package com.nexa.library.services;


import java.util.List;

import com.nexa.library.dtos.usuario.UsuarioRequest;
import com.nexa.library.dtos.usuario.UsuarioResponse;

public interface UsuarioService {

    UsuarioResponse crear(UsuarioRequest request);

    List<UsuarioResponse> listar();

    UsuarioResponse buscarPorId(Long id);

    UsuarioResponse actualizar(
        Long id,
        UsuarioRequest request
    );

    void eliminar(Long id);
}
