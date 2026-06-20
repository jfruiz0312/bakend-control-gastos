package com.fernandoruiz.app.management.mappers;

import com.fernandoruiz.app.management.dto.response.CompraDetalleResponse;
import com.fernandoruiz.app.management.dto.response.CompraResponse;
import com.fernandoruiz.app.management.dto.response.GastoOperativoResponse;
import com.fernandoruiz.app.management.model.Compra;
import com.fernandoruiz.app.management.model.CompraDetalle;
import com.fernandoruiz.app.management.model.GastoOperativo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompraMapper {
    CompraResponse toResponse(Compra compra);

    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(source = "producto.codigo", target = "codigoProducto")
    @Mapping(source = "producto.nombre", target = "nombreProducto")
    CompraDetalleResponse toResponse(CompraDetalle detalle);

    @Mapping(source = "compra.id", target = "compraId")
    GastoOperativoResponse toResponse(GastoOperativo gastoOperativo);
}
