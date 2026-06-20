package com.fernandoruiz.app.management.service;

import com.fernandoruiz.app.management.dto.request.VentaRequest;
import com.fernandoruiz.app.management.dto.response.VentaResponse;

import java.util.List;

public interface VentaService {
    VentaResponse registrarVenta(VentaRequest request);

    List<VentaResponse> listarVentas();
}
