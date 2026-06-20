package com.fernandoruiz.app.management.mappers;

import com.fernandoruiz.app.management.dto.response.VentaDetalleResponse;
import com.fernandoruiz.app.management.dto.response.VentaResponse;
import com.fernandoruiz.app.management.model.Venta;
import com.fernandoruiz.app.management.model.VentaDetalle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VentaMapper {
    VentaResponse toResponse(Venta venta);

    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(source = "producto.codigo", target = "codigoProducto")
    @Mapping(source = "producto.nombre", target = "nombreProducto")
    VentaDetalleResponse toResponse(VentaDetalle detalle);
}
