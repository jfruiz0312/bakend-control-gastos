package com.fernandoruiz.app.management.mappers;

import com.fernandoruiz.app.management.dto.response.GastoOperativoResponse;
import com.fernandoruiz.app.management.model.GastoOperativo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GastoOperativoMapper {
    @Mapping(source = "compra.id", target = "compraId")
    GastoOperativoResponse toResponse(GastoOperativo gastoOperativo);
}
