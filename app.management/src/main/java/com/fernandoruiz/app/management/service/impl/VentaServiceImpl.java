package com.fernandoruiz.app.management.service.impl;

import com.fernandoruiz.app.management.dto.request.VentaDetalleRequest;
import com.fernandoruiz.app.management.dto.request.VentaRequest;
import com.fernandoruiz.app.management.dto.response.VentaResponse;
import com.fernandoruiz.app.management.exception.ResourceNotFoundException;
import com.fernandoruiz.app.management.mappers.VentaMapper;
import com.fernandoruiz.app.management.model.MovimientoInventario;
import com.fernandoruiz.app.management.model.Producto;
import com.fernandoruiz.app.management.model.Venta;
import com.fernandoruiz.app.management.model.VentaDetalle;
import com.fernandoruiz.app.management.repository.MovimientoInventarioRepository;
import com.fernandoruiz.app.management.repository.ProductoRepository;
import com.fernandoruiz.app.management.repository.VentaRepository;
import com.fernandoruiz.app.management.service.VentaService;
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
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final VentaMapper ventaMapper;

    @Override
    @Transactional
    public VentaResponse registrarVenta(VentaRequest request) {
        log.info("Registrando venta para cliente {}", request.getCliente());

        Venta venta = Venta.builder()
                .cliente(request.getCliente().trim())
                .fechaVenta(request.getFechaVenta() != null ? request.getFechaVenta() : LocalDateTime.now())
                .totalVenta(BigDecimal.ZERO)
                .gananciaTotal(BigDecimal.ZERO)
                .build();

        BigDecimal totalVenta = BigDecimal.ZERO;
        BigDecimal gananciaTotal = BigDecimal.ZERO;

        for (VentaDetalleRequest detalleRequest : request.getDetalles()) {
            Producto producto = productoRepository.findById(detalleRequest.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + detalleRequest.getProductoId()));

            validarProductoDisponible(producto, detalleRequest.getCantidad());

            BigDecimal precioVentaUnitario = MonetaryUtils.scale(detalleRequest.getPrecioVentaUnitario());
            BigDecimal costoRealUnitario = MonetaryUtils.safe(producto.getPrecioCompra());
            BigDecimal subtotalVenta = MonetaryUtils.scale(
                    precioVentaUnitario.multiply(BigDecimal.valueOf(detalleRequest.getCantidad()))
            );
            BigDecimal gananciaUnitaria = MonetaryUtils.scale(precioVentaUnitario.subtract(costoRealUnitario));
            BigDecimal gananciaDetalle = MonetaryUtils.scale(
                    gananciaUnitaria.multiply(BigDecimal.valueOf(detalleRequest.getCantidad()))
            );

            VentaDetalle detalle = VentaDetalle.builder()
                    .producto(producto)
                    .cantidad(detalleRequest.getCantidad())
                    .precioVentaUnitario(precioVentaUnitario)
                    .subtotalVenta(subtotalVenta)
                    .costoRealUnitario(costoRealUnitario)
                    .gananciaUnitaria(gananciaUnitaria)
                    .gananciaTotal(gananciaDetalle)
                    .build();
            venta.addDetalle(detalle);

            producto.setStockActual(producto.getStockActual() - detalleRequest.getCantidad());
            totalVenta = totalVenta.add(subtotalVenta);
            gananciaTotal = gananciaTotal.add(gananciaDetalle);
        }

        venta.setTotalVenta(MonetaryUtils.scale(totalVenta));
        venta.setGananciaTotal(MonetaryUtils.scale(gananciaTotal));

        Venta ventaGuardada = ventaRepository.save(venta);
        registrarMovimientosSalida(ventaGuardada);

        log.info("Venta {} registrada con total {}", ventaGuardada.getId(), ventaGuardada.getTotalVenta());
        return ventaMapper.toResponse(ventaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listarVentas() {
        return ventaRepository.findAllByOrderByFechaVentaDesc()
                .stream()
                .map(ventaMapper::toResponse)
                .toList();
    }

    private void validarProductoDisponible(Producto producto, int cantidadSolicitada) {
        if (Boolean.FALSE.equals(producto.getActivo())) {
            throw new IllegalArgumentException("El producto " + producto.getNombre() + " se encuentra inactivo");
        }
        if (producto.getStockActual() == null || producto.getStockActual() < cantidadSolicitada) {
            throw new IllegalArgumentException("Stock insuficiente para el producto " + producto.getNombre());
        }
    }

    private void registrarMovimientosSalida(Venta venta) {
        for (VentaDetalle detalle : venta.getDetalles()) {
            MovimientoInventario movimiento = new MovimientoInventario();
            movimiento.setProducto(detalle.getProducto());
            movimiento.setTipo(MovimientoInventario.TipoMovimiento.SALIDA);
            movimiento.setCantidad(detalle.getCantidad());
            movimiento.setPrecioUnitario(detalle.getPrecioVentaUnitario());
            movimiento.setObservacion("Venta #" + venta.getId() + " - " + venta.getCliente());
            movimientoInventarioRepository.save(movimiento);
        }
    }
}
