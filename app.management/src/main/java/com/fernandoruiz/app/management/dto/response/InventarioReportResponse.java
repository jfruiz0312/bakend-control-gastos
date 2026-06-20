package com.fernandoruiz.app.management.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class InventarioReportResponse {
    private Long totalProductos;
    private Long totalProductosActivos;
    private Integer totalUnidadesStock;
    private BigDecimal valorInventarioCosto;
    private BigDecimal valorInventarioVenta;
    private List<InventarioProductoResponse> productosStockBajo;
}
