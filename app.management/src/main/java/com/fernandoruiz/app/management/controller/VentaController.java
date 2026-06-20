package com.fernandoruiz.app.management.controller;

import com.fernandoruiz.app.management.dto.request.VentaRequest;
import com.fernandoruiz.app.management.dto.response.ApiResponse;
import com.fernandoruiz.app.management.dto.response.VentaResponse;
import com.fernandoruiz.app.management.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@Tag(name = "Ventas", description = "Gestión de ventas y cálculo de ganancias")
public class VentaController {

    private final VentaService ventaService;

    @PostMapping
    @Operation(summary = "Registrar una venta")
    public ResponseEntity<ApiResponse<VentaResponse>> registrarVenta(@Valid @RequestBody VentaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Venta registrada correctamente", ventaService.registrarVenta(request)));
    }

    @GetMapping
    @Operation(summary = "Listar las ventas registradas")
    public ResponseEntity<ApiResponse<List<VentaResponse>>> listarVentas() {
        return ResponseEntity.ok(ApiResponse.success("Ventas obtenidas correctamente", ventaService.listarVentas()));
    }
}
