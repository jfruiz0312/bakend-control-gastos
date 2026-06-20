package com.fernandoruiz.app.management.dto.request;

import com.fernandoruiz.app.management.enums.TipoGastoOperativo;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GastoOperativoRequest {
    @NotBlank(message = "La descripción del gasto es obligatoria")
    private String descripcion;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    private LocalDateTime fecha;

    @NotNull(message = "El tipo de gasto es obligatorio")
    private TipoGastoOperativo tipoGasto;
}
