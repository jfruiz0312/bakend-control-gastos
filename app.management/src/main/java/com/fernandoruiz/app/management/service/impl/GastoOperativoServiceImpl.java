package com.fernandoruiz.app.management.service.impl;

import com.fernandoruiz.app.management.dto.request.GastoOperativoRequest;
import com.fernandoruiz.app.management.dto.response.GastoOperativoResponse;
import com.fernandoruiz.app.management.mappers.GastoOperativoMapper;
import com.fernandoruiz.app.management.model.GastoOperativo;
import com.fernandoruiz.app.management.repository.GastoOperativoRepository;
import com.fernandoruiz.app.management.service.GastoOperativoService;
import com.fernandoruiz.app.management.util.MonetaryUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GastoOperativoServiceImpl implements GastoOperativoService {

    private final GastoOperativoRepository gastoOperativoRepository;
    private final GastoOperativoMapper gastoOperativoMapper;

    @Override
    @Transactional
    public GastoOperativoResponse registrarGasto(GastoOperativoRequest request) {
        log.info("Registrando gasto operativo de tipo {}", request.getTipoGasto());

        GastoOperativo gastoOperativo = GastoOperativo.builder()
                .descripcion(request.getDescripcion().trim())
                .monto(MonetaryUtils.scale(request.getMonto()))
                .fecha(request.getFecha() != null ? request.getFecha() : LocalDateTime.now())
                .tipoGasto(request.getTipoGasto())
                .build();

        return gastoOperativoMapper.toResponse(gastoOperativoRepository.save(gastoOperativo));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GastoOperativoResponse> listarGastos() {
        return gastoOperativoRepository.findAllByOrderByFechaDesc()
                .stream()
                .map(gastoOperativoMapper::toResponse)
                .toList();
    }
}
