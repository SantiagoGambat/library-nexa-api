package com.nexa.library.services;



import java.util.List;

import com.nexa.library.dtos.prestamo.PrestamoRequest;
import com.nexa.library.dtos.prestamo.PrestamoResponse;

public interface PrestamoService {

    PrestamoResponse crear(PrestamoRequest request);

    List<PrestamoResponse> listar();

    List<PrestamoResponse> listarPorUsuario(
        Long usuarioId
    );

    List<PrestamoResponse> listarPorLibro(
        Long libroId
    );

    List<PrestamoResponse> listarPorIsbn(
        String isbn
    );
}
