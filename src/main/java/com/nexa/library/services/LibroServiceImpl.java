package com.nexa.library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexa.library.dtos.ejemplar.EjemplarResponse;
import com.nexa.library.dtos.libro.LibroRequest;
import com.nexa.library.dtos.libro.LibroResponse;
import com.nexa.library.exceptions.RecursoNoEncontradoException;
import com.nexa.library.exceptions.ReglaNegocioException;
import com.nexa.library.mappers.LibroMapper;
import com.nexa.library.models.Ejemplar;
import com.nexa.library.models.Libro;
import com.nexa.library.models.enums.EstadoPrestamo;
import com.nexa.library.repositroy.EjemplarRepository;
import com.nexa.library.repositroy.LibroRepository;
import com.nexa.library.repositroy.PrestamoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;
    private final EjemplarRepository ejemplarRepository;
    private final PrestamoRepository prestamoRepository;

    @Override
    public LibroResponse crear(LibroRequest request) {

        if (libroRepository.existsByIsbn(request.getIsbn())) {
            throw new ReglaNegocioException(
                    "Ya existe un libro con el ISBN: "
                            + request.getIsbn());
        }

        Libro libro = LibroMapper.INSTANCE.toEntity(request);

        return LibroMapper.INSTANCE.toResponse(
                libroRepository.save(libro));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibroResponse> listar() {

        return libroRepository.findAll()
                .stream()
                .map(LibroMapper.INSTANCE::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibroResponse> listarConEjemplarDisponible() {

        List<EstadoPrestamo> estadosNoDisponibles = List.of(
                EstadoPrestamo.ACTIVO,
                EstadoPrestamo.PROGRAMADO);

        return libroRepository
                .findLibrosConEjemplarDisponible(estadosNoDisponibles)
                .stream()
                .map(LibroMapper.INSTANCE::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LibroResponse buscarPorId(Long id) {

        return LibroMapper.INSTANCE.toResponse(
                obtenerLibro(id));
    }

    @Override
    public LibroResponse actualizar(
            Long id,
            LibroRequest request) {

        Libro libro = obtenerLibro(id);

        if (!libro.getIsbn().equals(request.getIsbn())
                && libroRepository.existsByIsbn(request.getIsbn())) {

            throw new ReglaNegocioException(
                    "Ya existe un libro con el ISBN: "
                            + request.getIsbn());
        }

        LibroMapper.INSTANCE.updateEntity(request, libro);

        return LibroMapper.INSTANCE.toResponse(libro);
    }

    @Override
    public void eliminar(Long id) {

        Libro libro = obtenerLibro(id);

        boolean tieneEjemplares = ejemplarRepository.existsByLibroId(id);

        if (tieneEjemplares) {
            throw new ReglaNegocioException(
                    "No se puede eliminar el libro porque tiene ejemplares asociados.");
        }

        libroRepository.delete(libro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EjemplarResponse> listarEjemplaresDisponibles(
            String isbn) {

        List<Ejemplar> ejemplares = ejemplarRepository.findByLibroIsbn(isbn);

        return ejemplares.stream()
                .filter(ejemplar -> !prestamoRepository
                        .existsByEjemplarIdAndEstadoPrestamo(
                                ejemplar.getId(),
                                EstadoPrestamo.ACTIVO))
                .map(ejemplar -> new EjemplarResponse(
                        ejemplar.getId(),
                        ejemplar.getCodigoInventario(),
                        ejemplar.getLibro().getId(),
                        ejemplar.getLibro().getTitulo(),
                        ejemplar.getLibro().getIsbn(),
                        true))
                .toList();
    }

    private Libro obtenerLibro(Long id) {

        return libroRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Libro no encontrado: " + id));
    }

}
