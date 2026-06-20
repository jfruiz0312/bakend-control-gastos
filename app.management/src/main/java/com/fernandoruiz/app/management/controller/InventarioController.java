package com.fernandoruiz.app.management.controller;

// controller/InventarioController.java

import com.fernandoruiz.app.management.dto.MovimientoInventarioDTO;
import com.fernandoruiz.app.management.model.MovimientoInventario;
import com.fernandoruiz.app.management.service.InventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioController {
    private final InventarioService inventarioService;

    @PostMapping("/movimiento")
    public ResponseEntity<MovimientoInventario> registrarMovimiento(
            @Valid @RequestBody MovimientoInventarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventarioService.registrarMovimiento(dto));
    }
}
