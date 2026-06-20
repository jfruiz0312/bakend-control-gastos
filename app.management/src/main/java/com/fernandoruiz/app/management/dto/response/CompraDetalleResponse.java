package com.fernandoruiz.app.management.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CompraDetalleResponse {
    private Long id;
    private Long productoId;
    private String codigoProducto;
    private String nombreProducto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private BigDecimal costoOperativoAsignado;
    private BigDecimal costoRealUnitario;
}
