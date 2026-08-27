package com.nexa.library.repositroy;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexa.library.models.Prestamo;
import com.nexa.library.models.enums.EstadoPrestamo;

public interface PrestamoRepository
        extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByUsuarioId(Long usuarioId);

    List<Prestamo> findByEjemplarLibroId(Long libroId);

    List<Prestamo> findByEjemplarLibroIsbn(String isbn);

    boolean existsByEjemplarId(Long ejemplarId);

    boolean existsByUsuarioIdAndEjemplarLibroIsbnAndEstadoPrestamoIn(
            Long usuarioId,
            String isbn,
            List<EstadoPrestamo> estados
    );

    boolean existsByUsuarioId(Long usuarioId);


    boolean existsByUsuarioIdAndEstadoPrestamo(
        Long usuarioId,
        EstadoPrestamo estadoPrestamo
    );

    boolean existsByEjemplarIdAndEstadoPrestamo(
        Long ejemplarId,
        EstadoPrestamo estadoPrestamo
    );
}
