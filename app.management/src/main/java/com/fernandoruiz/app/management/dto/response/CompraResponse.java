package com.fernandoruiz.app.management.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CompraResponse {
    private Long id;
    private String proveedor;
    private LocalDateTime fechaCompra;
    private BigDecimal subtotalCompra;
    private BigDecimal totalGastosAsociados;
    private BigDecimal totalCompra;
    private List<CompraDetalleResponse> detalles;
    private List<GastoOperativoResponse> gastosAsociados;
}
