package com.fernandoruiz.app.management.controller;

import com.fernandoruiz.app.management.dto.request.CompraRequest;
import com.fernandoruiz.app.management.dto.response.ApiResponse;
import com.fernandoruiz.app.management.dto.response.CompraResponse;
import com.fernandoruiz.app.management.service.CompraService;
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
@RequestMapping("/api/compras")
@RequiredArgsConstructor
@Tag(name = "Compras", description = "Gestión de compras de mercadería y sus gastos asociados")
public class CompraController {

    private final CompraService compraService;

    @PostMapping
    @Operation(summary = "Registrar una compra con sus detalles y gastos asociados")
    public ResponseEntity<ApiResponse<CompraResponse>> registrarCompra(@Valid @RequestBody CompraRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Compra registrada correctamente", compraService.registrarCompra(request)));
    }

    @GetMapping
    @Operation(summary = "Listar las compras registradas")
    public ResponseEntity<ApiResponse<List<CompraResponse>>> listarCompras() {
        return ResponseEntity.ok(ApiResponse.success("Compras obtenidas correctamente", compraService.listarCompras()));
    }
}
