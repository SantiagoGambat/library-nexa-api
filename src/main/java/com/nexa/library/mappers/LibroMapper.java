package com.nexa.library.mappers;



import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.nexa.library.dtos.libro.LibroRequest;
import com.nexa.library.dtos.libro.LibroResponse;
import com.nexa.library.models.Libro;

@Mapper(componentModel = "spring")
public interface LibroMapper {

    LibroMapper INSTANCE = Mappers.getMapper(LibroMapper.class);

    LibroResponse toResponse(Libro libro);

    Libro toEntity(LibroRequest request);

    void updateEntity(
        LibroRequest request,
        @MappingTarget Libro libro
    );
}
