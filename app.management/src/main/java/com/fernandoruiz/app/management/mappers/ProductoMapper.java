package com.fernandoruiz.app.management.mappers;

// mapper/ProductoMapper.java

import com.fernandoruiz.app.management.dto.ProductoDTO;
import com.fernandoruiz.app.management.model.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductoMapper {
    @Mapping(source = "categoria", target = "categoriaProducto")
    ProductoDTO toDTO(Producto producto);

    @Mapping(source = "categoriaProducto", target = "categoria")
    Producto toEntity(ProductoDTO productoDTO);
}