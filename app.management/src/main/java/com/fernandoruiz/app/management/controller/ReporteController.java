package com.fernandoruiz.app.management.controller;

import com.fernandoruiz.app.management.dto.response.ApiResponse;
import com.fernandoruiz.app.management.dto.response.GananciasReportResponse;
import com.fernandoruiz.app.management.dto.response.InventarioReportResponse;
import com.fernandoruiz.app.management.dto.response.VentasReportResponse;
import com.fernandoruiz.app.management.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "Consultas consolidadas de ganancias, inventario y ventas")
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/ganancias")
    @Operation(summary = "Obtener el reporte de ganancias")
    public ResponseEntity<ApiResponse<GananciasReportResponse>> obtenerReporteGanancias() {
        return ResponseEntity.ok(ApiResponse.success("Reporte de ganancias generado correctamente",
                reporteService.obtenerReporteGanancias()));
    }

    @GetMapping("/inventario")
    @Operation(summary = "Obtener el reporte de inventario")
    public ResponseEntity<ApiResponse<InventarioReportResponse>> obtenerReporteInventario() {
        return ResponseEntity.ok(ApiResponse.success("Reporte de inventario generado correctamente",
                reporteService.obtenerReporteInventario()));
    }

    @GetMapping("/ventas")
    @Operation(summary = "Obtener el reporte de ventas")
    public ResponseEntity<ApiResponse<VentasReportResponse>> obtenerReporteVentas() {
        return ResponseEntity.ok(ApiResponse.success("Reporte de ventas generado correctamente",
                reporteService.obtenerReporteVentas()));
    }
}
