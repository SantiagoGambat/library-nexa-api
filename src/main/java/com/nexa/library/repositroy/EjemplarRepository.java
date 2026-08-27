package com.nexa.library.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexa.library.models.Ejemplar;

import java.util.List;
import java.util.Optional;

public interface EjemplarRepository
        extends JpaRepository<Ejemplar, Long> {

    List<Ejemplar> findByLibroIsbn(String isbn);

    Optional<Ejemplar> findByCodigoInventario(
        String codigoInventario
    );

    boolean existsByCodigoInventario(
        String codigoInventario
    );

    boolean existsByCodigoInventarioAndIdNot(
        String codigoInventario,
        Long id
    );

    Optional<Ejemplar> findByCodigoInventarioAndLibroId(
        String codigoInventario,
        Long libroId
    );

    List<Ejemplar> findByLibroId(Long libroId);

    boolean existsByLibroId(Long libroId);
}