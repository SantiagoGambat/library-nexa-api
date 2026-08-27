package com.nexa.library.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nexa.library.models.Libro;
import com.nexa.library.models.enums.EstadoPrestamo;

import java.util.List;
import java.util.Optional;

public interface LibroRepository
        extends JpaRepository<Libro, Long> {

    Optional<Libro> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    @Query("""
        SELECT DISTINCT l
        FROM Libro l
        JOIN l.ejemplares e
        WHERE NOT EXISTS (
            SELECT p.id
            FROM Prestamo p
            WHERE p.ejemplar = e
            AND p.estadoPrestamo IN :estados
        )
        """)
    List<Libro> findLibrosConEjemplarDisponible(
            @Param("estados") List<EstadoPrestamo> estados);
}
