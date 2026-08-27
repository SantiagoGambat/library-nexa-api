package com.nexa.library.services;



import java.util.List;

import com.nexa.library.dtos.ejemplar.EjemplarResponse;
import com.nexa.library.dtos.libro.LibroRequest;
import com.nexa.library.dtos.libro.LibroResponse;

public interface LibroService {

    LibroResponse crear(LibroRequest request);

    List<LibroResponse> listar();

    LibroResponse buscarPorId(Long id);

    LibroResponse actualizar(
        Long id,
        LibroRequest request
    );

    void eliminar(Long id);

    List<EjemplarResponse> listarEjemplaresDisponibles(
        String isbn
    );

    List<LibroResponse> listarConEjemplarDisponible();

}
