package com.fernandoruiz.app.management.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class GananciaProductoResponse {
    private Long productoId;
    private String codigoProducto;
    private String nombreProducto;
    private Long unidadesVendidas;
    private BigDecimal ingresoTotal;
    private BigDecimal costoTotal;
    private BigDecimal gananciaTotal;
}
