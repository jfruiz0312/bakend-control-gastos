package com.fernandoruiz.app.management.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class VentaRequest {
    @NotBlank(message = "El cliente es obligatorio")
    private String cliente;

    private LocalDateTime fechaVenta;

    @Valid
    @NotEmpty(message = "Debe registrar al menos un detalle de venta")
    private List<VentaDetalleRequest> detalles = new ArrayList<>();
}
