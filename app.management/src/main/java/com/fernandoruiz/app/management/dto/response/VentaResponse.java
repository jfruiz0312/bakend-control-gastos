package com.fernandoruiz.app.management.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VentaResponse {
    private Long id;
    private String cliente;
    private LocalDateTime fechaVenta;
    private BigDecimal totalVenta;
    private BigDecimal gananciaTotal;
    private List<VentaDetalleResponse> detalles;
}
