package com.fernandoruiz.app.management.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class VentasReportResponse {
    private BigDecimal totalVentasHistoricas;
    private BigDecimal totalVentasMensuales;
    private BigDecimal totalVentasAnuales;
    private Long unidadesVendidasMensuales;
    private Long unidadesVendidasAnuales;
    private BigDecimal gananciaMensual;
    private BigDecimal gananciaAnual;
}
