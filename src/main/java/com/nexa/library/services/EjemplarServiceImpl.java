package com.nexa.library.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexa.library.dtos.ejemplar.EjemplarRequest;
import com.nexa.library.dtos.ejemplar.EjemplarResponse;
import com.nexa.library.exceptions.RecursoNoEncontradoException;
import com.nexa.library.exceptions.ReglaNegocioException;
import com.nexa.library.models.Ejemplar;
import com.nexa.library.models.Libro;
import com.nexa.library.models.enums.EstadoPrestamo;
import com.nexa.library.repositroy.EjemplarRepository;
import com.nexa.library.repositroy.LibroRepository;
import com.nexa.library.repositroy.PrestamoRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class EjemplarServiceImpl implements EjemplarService {

    private final EjemplarRepository ejemplarRepository;
    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;

    @Override
    public EjemplarResponse crear(
        EjemplarRequest request
    ) {

        if (ejemplarRepository.existsByCodigoInventario(
            request.getCodigoInventario()
        )) {

            throw new ReglaNegocioException(
                "Ya existe un ejemplar con el código de inventario: "
                    + request.getCodigoInventario()
            );
        }

        Libro libro = libroRepository.findById(
            request.getLibroId()
        ).orElseThrow(() ->
            new RecursoNoEncontradoException(
                "Libro no encontrado: "
                    + request.getLibroId()
            )
        );

        Ejemplar ejemplar = Ejemplar.builder()
            .codigoInventario(
                request.getCodigoInventario().trim()
            )
            .libro(libro)
            .build();

        ejemplar = ejemplarRepository.save(ejemplar);

        return toResponse(ejemplar);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EjemplarResponse> listar() {

        return ejemplarRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EjemplarResponse buscarPorId(Long id) {

        return toResponse(
            obtenerEjemplar(id)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<EjemplarResponse> listarPorLibro(
        Long libroId
    ) {

        if (!libroRepository.existsById(libroId)) {
            throw new RecursoNoEncontradoException(
                "Libro no encontrado: " + libroId
            );
        }

        return ejemplarRepository.findByLibroId(libroId)
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Override
    public EjemplarResponse actualizar(
        Long id,
        EjemplarRequest request
    ) {

        Ejemplar ejemplar = obtenerEjemplar(id);

        if (
            !ejemplar.getCodigoInventario()
                .equals(request.getCodigoInventario())
            &&
            ejemplarRepository
                .existsByCodigoInventarioAndIdNot(
                    request.getCodigoInventario(),
                    id
                )
        ) {

            throw new ReglaNegocioException(
                "Ya existe un ejemplar con el código de inventario: "
                    + request.getCodigoInventario()
            );
        }

        Libro libro = libroRepository.findById(
            request.getLibroId()
        ).orElseThrow(() ->
            new RecursoNoEncontradoException(
                "Libro no encontrado: "
                    + request.getLibroId()
            )
        );

        ejemplar.setCodigoInventario(
            request.getCodigoInventario().trim()
        );

        ejemplar.setLibro(libro);

        return toResponse(ejemplar);
    }

    @Override
    public void eliminar(Long id) {

        Ejemplar ejemplar = obtenerEjemplar(id);

        if (
            prestamoRepository
                .existsByEjemplarId(id)
        ) {

            throw new ReglaNegocioException(
                "No se puede eliminar el ejemplar porque tiene préstamos registrados."
            );
        }

        ejemplarRepository.delete(ejemplar);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EjemplarResponse> listarDisponiblesPorIsbn(
        String isbn
    ) {

        if (!libroRepository.existsByIsbn(isbn)) {
            throw new RecursoNoEncontradoException(
                "No existe un libro con el ISBN: " + isbn
            );
        }

        return ejemplarRepository.findByLibroIsbn(isbn)
            .stream()
            .filter(ejemplar ->
                !prestamoRepository
                    .existsByEjemplarIdAndEstadoPrestamo(
                        ejemplar.getId(),
                        EstadoPrestamo.ACTIVO
                    )
            )
            .map(this::toResponse)
            .toList();
    }

    private Ejemplar obtenerEjemplar(Long id) {

        return ejemplarRepository.findById(id)
            .orElseThrow(() ->
                new RecursoNoEncontradoException(
                    "Ejemplar no encontrado: " + id
                )
            );
    }

    private EjemplarResponse toResponse(
        Ejemplar ejemplar
    ) {

        boolean disponible =
            !prestamoRepository
                .existsByEjemplarIdAndEstadoPrestamo(
                    ejemplar.getId(),
                    EstadoPrestamo.ACTIVO
                );

        return new EjemplarResponse(
            ejemplar.getId(),
            ejemplar.getCodigoInventario(),
            ejemplar.getLibro().getId(),
            ejemplar.getLibro().getIsbn(),
            ejemplar.getLibro().getTitulo(),
            disponible
        );
    }
}