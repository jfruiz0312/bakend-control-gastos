package com.fernandoruiz.app.management.service;

import com.fernandoruiz.app.management.dto.request.GastoOperativoRequest;
import com.fernandoruiz.app.management.dto.response.GastoOperativoResponse;

import java.util.List;

public interface GastoOperativoService {
    GastoOperativoResponse registrarGasto(GastoOperativoRequest request);

    List<GastoOperativoResponse> listarGastos();
}
