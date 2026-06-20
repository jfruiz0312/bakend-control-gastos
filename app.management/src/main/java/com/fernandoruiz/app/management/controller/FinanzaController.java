package com.fernandoruiz.app.management.controller;

// controller/FinanzaController.java (versión actualizada)

import com.fernandoruiz.app.management.dto.TransaccionDTO;
import com.fernandoruiz.app.management.service.FinanzaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/finanzas")
@RequiredArgsConstructor
public class FinanzaController {
    private final FinanzaService finanzaService;

    @PostMapping("/transaccion")
    public ResponseEntity<TransaccionDTO> registrarTransaccion(
            @Valid @RequestBody TransaccionDTO transaccionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(finanzaService.registrarTransaccion(transaccionDTO));
    }

    @GetMapping("/resumen")
    public ResponseEntity<Map<String, BigDecimal>> obtenerResumen() {
        return ResponseEntity.ok(finanzaService.obtenerResumenFinanciero());
    }
}