package com.fernandoruiz.app.management.service;

// service/FinanzaService.java

import com.fernandoruiz.app.management.dto.TransaccionDTO;
import com.fernandoruiz.app.management.mappers.TransaccionMapper;
import com.fernandoruiz.app.management.model.Transaccion;
import com.fernandoruiz.app.management.enums.TipoTransaccion;
import com.fernandoruiz.app.management.repository.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FinanzaService {
    private final TransaccionRepository transaccionRepository;
    private final TransaccionMapper transaccionMapper;

    @Transactional
    public TransaccionDTO registrarTransaccion(TransaccionDTO transaccionDTO) {
        Transaccion transaccion = transaccionMapper.toEntity(transaccionDTO);
        transaccion = transaccionRepository.save(transaccion);
        return transaccionMapper.toDTO(transaccion);
    }

    public Map<String, BigDecimal> obtenerResumenFinanciero() {
        Map<String, BigDecimal> resumen = new HashMap<>();

        BigDecimal totalIngresos = transaccionRepository.sumByTipo(TipoTransaccion.INGRESO);
        BigDecimal totalGastos = transaccionRepository.sumByTipo(TipoTransaccion.GASTO);

        BigDecimal ingresos = totalIngresos != null ? totalIngresos : BigDecimal.ZERO;
        BigDecimal gastos = totalGastos != null ? totalGastos : BigDecimal.ZERO;

        resumen.put("totalIngresos", ingresos);
        resumen.put("totalGastos", gastos);
        resumen.put("balance", ingresos.subtract(gastos));

        return resumen;
    }
}