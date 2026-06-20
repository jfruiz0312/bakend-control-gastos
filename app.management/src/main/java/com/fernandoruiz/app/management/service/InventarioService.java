package com.fernandoruiz.app.management.service;


// service/InventarioService.java


import com.fernandoruiz.app.management.dto.MovimientoInventarioDTO;
import com.fernandoruiz.app.management.exception.ResourceNotFoundException;
import com.fernandoruiz.app.management.model.MovimientoInventario;
import com.fernandoruiz.app.management.model.Producto;
import com.fernandoruiz.app.management.repository.MovimientoInventarioRepository;
import com.fernandoruiz.app.management.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioService {
    private final MovimientoInventarioRepository movimientoRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    public MovimientoInventario registrarMovimiento(MovimientoInventarioDTO dto) {
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setTipo(MovimientoInventario.TipoMovimiento.valueOf(dto.getTipo()));
        movimiento.setCantidad(dto.getCantidad());
        movimiento.setPrecioUnitario(dto.getPrecioUnitario());
        movimiento.setObservacion(dto.getObservacion());

        // Actualizar stock
        actualizarStock(producto, movimiento);

        productoRepository.save(producto);
        return movimientoRepository.save(movimiento);
    }

    private void actualizarStock(Producto producto, MovimientoInventario movimiento) {
        switch (movimiento.getTipo()) {
            case ENTRADA:
                producto.setStockActual(producto.getStockActual() + movimiento.getCantidad());
                break;
            case SALIDA:
                if (producto.getStockActual() < movimiento.getCantidad()) {
                    throw new IllegalArgumentException("Stock insuficiente para la salida");
                }
                producto.setStockActual(producto.getStockActual() - movimiento.getCantidad());
                break;
            case AJUSTE:
                producto.setStockActual(movimiento.getCantidad());
                break;
        }
    }
}
