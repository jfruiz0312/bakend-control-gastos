package com.fernandoruiz.app.management.model.converter;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BooleanToShortConverter implements AttributeConverter<Boolean, Short> {

    private static final short TRUE_VALUE = 1;
    private static final short FALSE_VALUE = 0;

    @Override
    public Short convertToDatabaseColumn(Boolean attribute) {
        return Boolean.TRUE.equals(attribute) ? TRUE_VALUE : FALSE_VALUE;
    }

    @Override
    public Boolean convertToEntityAttribute(Short dbData) {
        return dbData != null && dbData == TRUE_VALUE;
    }
}
