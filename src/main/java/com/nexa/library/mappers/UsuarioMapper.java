package com.nexa.library.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.nexa.library.dtos.usuario.UsuarioRequest;
import com.nexa.library.dtos.usuario.UsuarioResponse;
import com.nexa.library.models.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    UsuarioResponse toResponse(Usuario usuario);

    Usuario toEntity(UsuarioRequest request);

    void updateEntity(
        UsuarioRequest request,
        @MappingTarget Usuario usuario
    );
}
