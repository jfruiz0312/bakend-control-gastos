package com.fernandoruiz.app.management.service;

import com.fernandoruiz.app.management.dto.request.CompraRequest;
import com.fernandoruiz.app.management.dto.response.CompraResponse;

import java.util.List;

public interface CompraService {
    CompraResponse registrarCompra(CompraRequest request);

    List<CompraResponse> listarCompras();
}
