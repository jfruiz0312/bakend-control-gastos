package com.fernandoruiz.app.management.service.impl;

import com.fernandoruiz.app.management.dto.response.GananciaProductoResponse;
import com.fernandoruiz.app.management.dto.response.GananciasReportResponse;
import com.fernandoruiz.app.management.dto.response.InventarioProductoResponse;
import com.fernandoruiz.app.management.dto.response.InventarioReportResponse;
import com.fernandoruiz.app.management.dto.response.VentasReportResponse;
import com.fernandoruiz.app.management.model.Producto;
import com.fernandoruiz.app.management.repository.ProductoRepository;
import com.fernandoruiz.app.management.repository.VentaDetalleRepository;
import com.fernandoruiz.app.management.repository.VentaRepository;
import com.fernandoruiz.app.management.service.ReporteService;
import com.fernandoruiz.app.management.util.MonetaryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final VentaRepository ventaRepository;
    private final VentaDetalleRepository ventaDetalleRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public GananciasReportResponse obtenerReporteGanancias() {
        LocalDateTime inicioMes = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime inicioAnio = LocalDate.now().withDayOfYear(1).atStartOfDay();
        LocalDateTime ahora = LocalDateTime.now();

        List<GananciaProductoResponse> gananciasPorProducto = ventaDetalleRepository.obtenerGananciasPorProducto()
                .stream()
                .map(this::mapGananciaProducto)
                .toList();

        return GananciasReportResponse.builder()
                .gananciaTotalHistorica(MonetaryUtils.safe(ventaDetalleRepository.sumGananciaTotal()))
                .gananciaMensualActual(MonetaryUtils.safe(ventaDetalleRepository.sumGananciaTotalByFecha(inicioMes, ahora)))
                .gananciaAnualActual(MonetaryUtils.safe(ventaDetalleRepository.sumGananciaTotalByFecha(inicioAnio, ahora)))
                .gananciasPorProducto(gananciasPorProducto)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioReportResponse obtenerReporteInventario() {
        List<Producto> productos = productoRepository.findAll();
        List<InventarioProductoResponse> productosStockBajo = productoRepository.findProductosConStockBajo()
                .stream()
                .map(this::mapInventarioProducto)
                .toList();

        long totalProductosActivos = productos.stream()
                .filter(producto -> !Boolean.FALSE.equals(producto.getActivo()))
                .count();
        int totalUnidades = productos.stream()
                .map(Producto::getStockActual)
                .filter(stock -> stock != null)
                .mapToInt(Integer::intValue)
                .sum();
        BigDecimal valorInventarioCosto = productos.stream()
                .map(producto -> MonetaryUtils.safe(producto.getPrecioCompra())
                        .multiply(BigDecimal.valueOf(producto.getStockActual() != null ? producto.getStockActual() : 0)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal valorInventarioVenta = productos.stream()
                .map(producto -> MonetaryUtils.safe(producto.getPrecioVenta())
                        .multiply(BigDecimal.valueOf(producto.getStockActual() != null ? producto.getStockActual() : 0)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return InventarioReportResponse.builder()
                .totalProductos((long) productos.size())
                .totalProductosActivos(totalProductosActivos)
                .totalUnidadesStock(totalUnidades)
                .valorInventarioCosto(MonetaryUtils.scale(valorInventarioCosto))
                .valorInventarioVenta(MonetaryUtils.scale(valorInventarioVenta))
                .productosStockBajo(productosStockBajo)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public VentasReportResponse obtenerReporteVentas() {
        LocalDateTime inicioMes = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime inicioAnio = LocalDate.now().withDayOfYear(1).atStartOfDay();
        LocalDateTime ahora = LocalDateTime.now();

        return VentasReportResponse.builder()
                .totalVentasHistoricas(MonetaryUtils.safe(ventaRepository.sumTotalVentaByFechaVentaBetween(LocalDateTime.of(2000, 1, 1, 0, 0), ahora)))
                .totalVentasMensuales(MonetaryUtils.safe(ventaRepository.sumTotalVentaByFechaVentaBetween(inicioMes, ahora)))
                .totalVentasAnuales(MonetaryUtils.safe(ventaRepository.sumTotalVentaByFechaVentaBetween(inicioAnio, ahora)))
                .unidadesVendidasMensuales(ventaDetalleRepository.sumCantidadByFecha(inicioMes, ahora))
                .unidadesVendidasAnuales(ventaDetalleRepository.sumCantidadByFecha(inicioAnio, ahora))
                .gananciaMensual(MonetaryUtils.safe(ventaDetalleRepository.sumGananciaTotalByFecha(inicioMes, ahora)))
                .gananciaAnual(MonetaryUtils.safe(ventaDetalleRepository.sumGananciaTotalByFecha(inicioAnio, ahora)))
                .build();
    }

    private GananciaProductoResponse mapGananciaProducto(Object[] row) {
        return GananciaProductoResponse.builder()
                .productoId((Long) row[0])
                .codigoProducto((String) row[1])
                .nombreProducto((String) row[2])
                .unidadesVendidas((Long) row[3])
                .ingresoTotal(MonetaryUtils.safe((BigDecimal) row[4]))
                .costoTotal(MonetaryUtils.safe((BigDecimal) row[5]))
                .gananciaTotal(MonetaryUtils.safe((BigDecimal) row[6]))
                .build();
    }

    private InventarioProductoResponse mapInventarioProducto(Producto producto) {
        int stockActual = producto.getStockActual() != null ? producto.getStockActual() : 0;
        return InventarioProductoResponse.builder()
                .id(producto.getId())
                .codigo(producto.getCodigo())
                .nombre(producto.getNombre())
                .stockActual(stockActual)
                .stockMinimo(producto.getStockMinimo())
                .precioCompra(MonetaryUtils.safe(producto.getPrecioCompra()))
                .precioVenta(MonetaryUtils.safe(producto.getPrecioVenta()))
                .valorInventarioCosto(MonetaryUtils.scale(
                        MonetaryUtils.safe(producto.getPrecioCompra()).multiply(BigDecimal.valueOf(stockActual))
                ))
                .valorInventarioVenta(MonetaryUtils.scale(
                        MonetaryUtils.safe(producto.getPrecioVenta()).multiply(BigDecimal.valueOf(stockActual))
                ))
                .build();
    }
}
