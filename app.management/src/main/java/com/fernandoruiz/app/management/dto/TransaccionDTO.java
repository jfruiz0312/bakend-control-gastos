package com.fernandoruiz.app.management.dto;

// dto/TransaccionDTO.java

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransaccionDTO {
    private Long id;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 200, message = "La descripción no puede tener más de 200 caracteres")
    private String descripcion;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotBlank(message = "El tipo de transacción es obligatorio")
    @Pattern(regexp = "INGRESO|GASTO", message = "El tipo de transacción debe ser INGRESO o GASTO")
    private String tipo; // INGRESO o GASTO

    private String categoria;
}