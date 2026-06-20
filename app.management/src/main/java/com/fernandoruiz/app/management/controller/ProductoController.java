package com.fernandoruiz.app.management.controller;

import com.fernandoruiz.app.management.dto.ProductoDTO;
import com.fernandoruiz.app.management.dto.response.ApiResponse;
import com.fernandoruiz.app.management.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Gestión del catálogo de productos")
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    @Operation(summary = "Listar productos")
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> obtenerProductos() {
        return ResponseEntity.ok(ApiResponse.success("Productos obtenidos correctamente", productoService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por id")
    public ResponseEntity<ApiResponse<ProductoDTO>> obtenerProductoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Producto obtenido correctamente", productoService.findById(id)));
    }

    @GetMapping("/stock-bajo")
    @Operation(summary = "Obtener productos con stock bajo")
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> obtenerProductosConStockBajo() {
        return ResponseEntity.ok(ApiResponse.success("Productos con stock bajo obtenidos correctamente",
                productoService.findStockBajo()));
    }

    @PostMapping
    @Operation(summary = "Crear producto")
    public ResponseEntity<ApiResponse<ProductoDTO>> crearProducto(@Valid @RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Producto creado correctamente", productoService.create(productoDTO)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto")
    public ResponseEntity<ApiResponse<ProductoDTO>> actualizarProducto(@PathVariable Long id,
                                                                       @Valid @RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.ok(ApiResponse.success("Producto actualizado correctamente",
                productoService.update(id, productoDTO)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto")
    public ResponseEntity<ApiResponse<Void>> eliminarProducto(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Producto eliminado correctamente", null));
    }
}
