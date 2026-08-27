package com.nexa.library.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.nexa.library.dtos.prestamo.PrestamoResponse;
import com.nexa.library.models.Prestamo;

@Mapper(componentModel = "spring")
public interface PrestamoMapper {

    PrestamoMapper INSTANCE = Mappers.getMapper(PrestamoMapper.class);

    @Mapping(
        target = "usuarioId",
        source = "usuario.id"
    )
    @Mapping(
        target = "ejemplarId",
        source = "ejemplar.id"
    )
    @Mapping(
        target = "codigoInventario",
        source = "ejemplar.codigoInventario"
    )
    @Mapping(
        target = "isbn",
        source = "ejemplar.libro.isbn"
    )
    PrestamoResponse toResponse(Prestamo prestamo);
}
