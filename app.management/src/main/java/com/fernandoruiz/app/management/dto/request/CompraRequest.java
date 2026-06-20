package com.fernandoruiz.app.management.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CompraRequest {
    @NotBlank(message = "El proveedor es obligatorio")
    private String proveedor;

    private LocalDateTime fechaCompra;

    @Valid
    @NotEmpty(message = "Debe registrar al menos un detalle de compra")
    private List<CompraDetalleRequest> detalles = new ArrayList<>();

    @Valid
    private List<GastoOperativoRequest> gastosAsociados = new ArrayList<>();
}
