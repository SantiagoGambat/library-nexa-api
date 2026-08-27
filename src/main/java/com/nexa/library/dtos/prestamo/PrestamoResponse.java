package com.nexa.library.dtos.prestamo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.nexa.library.models.enums.EstadoPrestamo;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrestamoResponse {

    private Long id;

    private LocalDateTime fechaPrestamo;

    private LocalDateTime fechaDevolucion;

    private Long usuarioId;

    private Long ejemplarId;

    private String codigoInventario;

    private String isbn;

    private EstadoPrestamo estadoPrestamo;
}
