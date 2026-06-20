package com.fernandoruiz.app.management.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VentaDetalleResponse {
    private Long id;
    private Long productoId;
    private String codigoProducto;
    private String nombreProducto;
    private Integer cantidad;
    private BigDecimal precioVentaUnitario;
    private BigDecimal subtotalVenta;
    private BigDecimal costoRealUnitario;
    private BigDecimal gananciaUnitaria;
    private BigDecimal gananciaTotal;
}
