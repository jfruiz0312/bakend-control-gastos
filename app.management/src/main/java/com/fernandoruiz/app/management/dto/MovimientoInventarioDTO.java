package com.fernandoruiz.app.management.dto;


// dto/MovimientoInventarioDTO.java

import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class MovimientoInventarioDTO {
    private Long id;

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @NotBlank(message = "El tipo de movimiento es obligatorio")
    private String tipo;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    private BigDecimal precioUnitario;

    private String observacion;
}