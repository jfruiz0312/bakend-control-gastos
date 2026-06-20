package com.fernandoruiz.app.management.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class GananciasReportResponse {
    private BigDecimal gananciaTotalHistorica;
    private BigDecimal gananciaMensualActual;
    private BigDecimal gananciaAnualActual;
    private List<GananciaProductoResponse> gananciasPorProducto;
}
