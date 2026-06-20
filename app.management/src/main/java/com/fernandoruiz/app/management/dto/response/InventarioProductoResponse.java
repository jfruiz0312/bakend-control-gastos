package com.fernandoruiz.app.management.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class InventarioProductoResponse {
    private Long id;
    private String codigo;
    private String nombre;
    private Integer stockActual;
    private Integer stockMinimo;
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private BigDecimal valorInventarioCosto;
    private BigDecimal valorInventarioVenta;
}
