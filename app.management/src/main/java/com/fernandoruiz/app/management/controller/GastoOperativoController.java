package com.fernandoruiz.app.management.controller;

import com.fernandoruiz.app.management.dto.request.GastoOperativoRequest;
import com.fernandoruiz.app.management.dto.response.ApiResponse;
import com.fernandoruiz.app.management.dto.response.GastoOperativoResponse;
import com.fernandoruiz.app.management.service.GastoOperativoService;
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
@RequestMapping("/api/gastos")
@RequiredArgsConstructor
@Tag(name = "Gastos Operativos", description = "Gestión de gastos operativos del negocio")
public class GastoOperativoController {

    private final GastoOperativoService gastoOperativoService;

    @PostMapping
    @Operation(summary = "Registrar un gasto operativo")
    public ResponseEntity<ApiResponse<GastoOperativoResponse>> registrarGasto(
            @Valid @RequestBody GastoOperativoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Gasto operativo registrado correctamente",
                        gastoOperativoService.registrarGasto(request)));
    }

    @GetMapping
    @Operation(summary = "Listar los gastos operativos registrados")
    public ResponseEntity<ApiResponse<List<GastoOperativoResponse>>> listarGastos() {
        return ResponseEntity.ok(ApiResponse.success("Gastos operativos obtenidos correctamente",
                gastoOperativoService.listarGastos()));
    }
}
