package com.nexa.library.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.nexa.library.dtos.ejemplar.EjemplarResponse;
import com.nexa.library.models.Ejemplar;

@Mapper(componentModel = "spring")
public interface EjemplarMapper {

    EjemplarMapper INSTANCE = Mappers.getMapper(EjemplarMapper.class);


    @Mapping(
        target = "libroId",
        source = "libro.id"
    )
        @Mapping(
        target = "titulo",
        source = "libro.titulo"
    )
    @Mapping(
        target = "isbn",
        source = "libro.isbn"
    )
    @Mapping(
        target = "disponible",
        ignore = true
    )
    EjemplarResponse toResponse(Ejemplar ejemplar);
}
