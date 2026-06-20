package com.fernandoruiz.app.management.service;
// service/ProductoService.java
import com.fernandoruiz.app.management.dto.ProductoDTO;
import com.fernandoruiz.app.management.exception.ResourceNotFoundException;
import com.fernandoruiz.app.management.mappers.ProductoMapper;
import com.fernandoruiz.app.management.model.Producto;
import com.fernandoruiz.app.management.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    @Transactional(readOnly = true)
    public List<ProductoDTO> findAll() {
        return productoRepository.findAll().stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductoDTO findById(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        return productoMapper.toDTO(producto);
    }

    @Transactional
    public ProductoDTO create(ProductoDTO productoDTO) {
        if (productoRepository.findByCodigo(productoDTO.getCodigo()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un producto con el código: " + productoDTO.getCodigo());
        }
        Producto producto = productoMapper.toEntity(productoDTO);
        producto.setActivo(productoDTO.getActivo() != null ? productoDTO.getActivo() : Boolean.TRUE);
        producto = productoRepository.save(producto);
        return productoMapper.toDTO(producto);
    }

    @Transactional
    public ProductoDTO update(Long id, ProductoDTO productoDTO) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        if (!productoExistente.getCodigo().equals(productoDTO.getCodigo())
                && productoRepository.findByCodigo(productoDTO.getCodigo()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un producto con el código: " + productoDTO.getCodigo());
        }

        productoExistente.setCodigo(productoDTO.getCodigo());
        productoExistente.setNombre(productoDTO.getNombre());
        productoExistente.setDescripcion(productoDTO.getDescripcion());
        productoExistente.setStockActual(productoDTO.getStockActual());
        productoExistente.setStockMinimo(productoDTO.getStockMinimo());
        productoExistente.setPrecioCompra(productoDTO.getPrecioCompra());
        productoExistente.setPrecioVenta(productoDTO.getPrecioVenta());
        productoExistente.setCategoria(productoDTO.getCategoriaProducto());
        productoExistente.setActivo(productoDTO.getActivo() != null ? productoDTO.getActivo() : productoExistente.getActivo());

        productoExistente = productoRepository.save(productoExistente);
        return productoMapper.toDTO(productoExistente);
    }

    @Transactional
    public void delete(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> findStockBajo() {
        return productoRepository.findProductosConStockBajo().stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());
    }
}