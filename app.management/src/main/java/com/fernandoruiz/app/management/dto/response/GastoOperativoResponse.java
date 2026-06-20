package com.fernandoruiz.app.management.dto.response;

import com.fernandoruiz.app.management.enums.TipoGastoOperativo;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GastoOperativoResponse {
    private Long id;
    private String descripcion;
    private BigDecimal monto;
    private LocalDateTime fecha;
    private TipoGastoOperativo tipoGasto;
    private Long compraId;
}
