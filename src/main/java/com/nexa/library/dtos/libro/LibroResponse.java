package com.nexa.library.dtos.libro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LibroResponse {

    private Long id;

    private String titulo;

    private String isbn;

    private String edicion;

    private LocalDate fechaPublicacion;

    private String autor;
}
