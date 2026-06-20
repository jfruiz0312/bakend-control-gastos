package com.fernandoruiz.app.management.mappers;

// mapper/TransaccionMapper.java

import com.fernandoruiz.app.management.dto.TransaccionDTO;
import com.fernandoruiz.app.management.model.Transaccion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransaccionMapper {
    @Mapping(source = "tipoTransaccion", target = "tipo")
    TransaccionDTO toDTO(Transaccion transaccion);

    @Mapping(source = "tipo", target = "tipoTransaccion")
    Transaccion toEntity(TransaccionDTO transaccionDTO);
}