package com.fernandoruiz.app.management.service;

import com.fernandoruiz.app.management.dto.response.GananciasReportResponse;
import com.fernandoruiz.app.management.dto.response.InventarioReportResponse;
import com.fernandoruiz.app.management.dto.response.VentasReportResponse;

public interface ReporteService {
    GananciasReportResponse obtenerReporteGanancias();

    InventarioReportResponse obtenerReporteInventario();

    VentasReportResponse obtenerReporteVentas();
}
