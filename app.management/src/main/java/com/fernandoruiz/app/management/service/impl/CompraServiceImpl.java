package com.fernandoruiz.app.management.service.impl;

import com.fernandoruiz.app.management.dto.request.CompraDetalleRequest;
import com.fernandoruiz.app.management.dto.request.CompraRequest;
import com.fernandoruiz.app.management.dto.request.GastoOperativoRequest;
import com.fernandoruiz.app.management.dto.response.CompraResponse;
import com.fernandoruiz.app.management.exception.ResourceNotFoundException;
import com.fernandoruiz.app.management.mappers.CompraMapper;
import com.fernandoruiz.app.management.model.Compra;
import com.fernandoruiz.app.management.model.CompraDetalle;
import com.fernandoruiz.app.management.model.GastoOperativo;
import com.fernandoruiz.app.management.model.MovimientoInventario;
import com.fernandoruiz.app.management.model.Producto;
import com.fernandoruiz.app.management.repository.CompraRepository;
import com.fernandoruiz.app.management.repository.MovimientoInventarioRepository;
import com.fernandoruiz.app.management.repository.ProductoRepository;
import com.fernandoruiz.app.management.service.CompraService;
import com.fernandoruiz.app.management.util.MonetaryUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final ProductoRepository productoRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final CompraMapper compraMapper;

    @Override
    @Transactional
    public CompraResponse registrarCompra(CompraRequest request) {
        log.info("Registrando compra para proveedor {}", request.getProveedor());

        Compra compra = Compra.builder()
                .proveedor(request.getProveedor().trim())
                .fechaCompra(request.getFechaCompra() != null ? request.getFechaCompra() : LocalDateTime.now())
                .subtotalCompra(BigDecimal.ZERO)
                .totalGastosAsociados(BigDecimal.ZERO)
                .totalCompra(BigDecimal.ZERO)
                .build();

        BigDecimal subtotalCompra = BigDecimal.ZERO;
        for (CompraDetalleRequest detalleRequest : request.getDetalles()) {
            Producto producto = productoRepository.findById(detalleRequest.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + detalleRequest.getProductoId()));

            BigDecimal precioUnitario = MonetaryUtils.scale(detalleRequest.getPrecioUnitario());
            BigDecimal subtotalDetalle = MonetaryUtils.scale(
                    precioUnitario.multiply(BigDecimal.valueOf(detalleRequest.getCantidad()))
            );

            CompraDetalle detalle = CompraDetalle.builder()
                    .producto(producto)
                    .cantidad(detalleRequest.getCantidad())
                    .precioUnitario(precioUnitario)
                    .subtotal(subtotalDetalle)
                    .costoOperativoAsignado(BigDecimal.ZERO)
                    .costoRealUnitario(BigDecimal.ZERO)
                    .build();
            compra.addDetalle(detalle);
            subtotalCompra = subtotalCompra.add(subtotalDetalle);
        }

        subtotalCompra = MonetaryUtils.scale(subtotalCompra);
        BigDecimal totalGastos = registrarGastosAsociados(compra, request.getGastosAsociados());

        compra.setSubtotalCompra(subtotalCompra);
        compra.setTotalGastosAsociados(totalGastos);
        compra.setTotalCompra(MonetaryUtils.scale(subtotalCompra.add(totalGastos)));

        distribuirCostosOperativos(compra);
        Compra compraGuardada = compraRepository.save(compra);
        registrarMovimientosEntrada(compraGuardada);

        log.info("Compra {} registrada con total {}", compraGuardada.getId(), compraGuardada.getTotalCompra());
        return compraMapper.toResponse(compraGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompraResponse> listarCompras() {
        return compraRepository.findAllByOrderByFechaCompraDesc()
                .stream()
                .map(compraMapper::toResponse)
                .toList();
    }

    private BigDecimal registrarGastosAsociados(Compra compra, List<GastoOperativoRequest> gastosAsociados) {
        BigDecimal totalGastos = BigDecimal.ZERO;
        for (GastoOperativoRequest gastoRequest : gastosAsociados) {
            BigDecimal monto = MonetaryUtils.scale(gastoRequest.getMonto());
            GastoOperativo gastoOperativo = GastoOperativo.builder()
                    .descripcion(gastoRequest.getDescripcion().trim())
                    .monto(monto)
                    .fecha(gastoRequest.getFecha() != null ? gastoRequest.getFecha() : compra.getFechaCompra())
                    .tipoGasto(gastoRequest.getTipoGasto())
                    .build();
            compra.addGasto(gastoOperativo);
            totalGastos = totalGastos.add(monto);
        }
        return MonetaryUtils.scale(totalGastos);
    }

    private void distribuirCostosOperativos(Compra compra) {
        BigDecimal subtotalCompra = compra.getSubtotalCompra();
        BigDecimal totalGastos = compra.getTotalGastosAsociados();
        BigDecimal acumulado = BigDecimal.ZERO;

        for (int index = 0; index < compra.getDetalles().size(); index++) {
            CompraDetalle detalle = compra.getDetalles().get(index);
            BigDecimal costoOperativoAsignado;

            if (index == compra.getDetalles().size() - 1) {
                costoOperativoAsignado = MonetaryUtils.scale(totalGastos.subtract(acumulado));
            } else if (subtotalCompra.compareTo(BigDecimal.ZERO) == 0) {
                costoOperativoAsignado = BigDecimal.ZERO;
            } else {
                costoOperativoAsignado = MonetaryUtils.scale(
                        totalGastos.multiply(detalle.getSubtotal())
                                .divide(subtotalCompra, 8, java.math.RoundingMode.HALF_UP)
                );
                acumulado = acumulado.add(costoOperativoAsignado);
            }

            BigDecimal costoTotalDetalle = MonetaryUtils.scale(detalle.getSubtotal().add(costoOperativoAsignado));
            BigDecimal costoRealUnitario = MonetaryUtils.divide(costoTotalDetalle, detalle.getCantidad());

            detalle.setCostoOperativoAsignado(costoOperativoAsignado);
            detalle.setCostoRealUnitario(costoRealUnitario);
            actualizarCostoPromedioYStock(detalle.getProducto(), detalle.getCantidad(), costoRealUnitario);
        }
    }

    private void actualizarCostoPromedioYStock(Producto producto, int cantidadEntrada, BigDecimal costoRealUnitario) {
        int stockActual = producto.getStockActual() != null ? producto.getStockActual() : 0;
        BigDecimal costoActual = MonetaryUtils.safe(producto.getPrecioCompra());
        BigDecimal costoExistenteTotal = costoActual.multiply(BigDecimal.valueOf(stockActual));
        BigDecimal costoNuevaEntrada = costoRealUnitario.multiply(BigDecimal.valueOf(cantidadEntrada));
        int nuevoStock = stockActual + cantidadEntrada;

        producto.setStockActual(nuevoStock);
        producto.setPrecioCompra(MonetaryUtils.divide(costoExistenteTotal.add(costoNuevaEntrada), nuevoStock));
        if (producto.getActivo() == null) {
            producto.setActivo(Boolean.TRUE);
        }
    }

    private void registrarMovimientosEntrada(Compra compra) {
        for (CompraDetalle detalle : compra.getDetalles()) {
            MovimientoInventario movimiento = new MovimientoInventario();
            movimiento.setProducto(detalle.getProducto());
            movimiento.setTipo(MovimientoInventario.TipoMovimiento.ENTRADA);
            movimiento.setCantidad(detalle.getCantidad());
            movimiento.setPrecioUnitario(detalle.getCostoRealUnitario());
            movimiento.setObservacion("Compra #" + compra.getId() + " - " + compra.getProveedor());
            movimientoInventarioRepository.save(movimiento);
        }
    }
}
