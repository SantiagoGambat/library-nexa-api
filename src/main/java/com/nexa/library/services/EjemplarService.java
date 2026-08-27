package com.nexa.library.services;

import java.util.List;

import com.nexa.library.dtos.ejemplar.EjemplarRequest;
import com.nexa.library.dtos.ejemplar.EjemplarResponse;

public interface EjemplarService {

    EjemplarResponse crear(EjemplarRequest request);

    List<EjemplarResponse> listar();

    EjemplarResponse buscarPorId(Long id);

    List<EjemplarResponse> listarPorLibro(Long libroId);

    EjemplarResponse actualizar(
        Long id,
        EjemplarRequest request
    );

    void eliminar(Long id);

    List<EjemplarResponse> listarDisponiblesPorIsbn(
        String isbn
    );
}
