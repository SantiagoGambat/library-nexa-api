package com.nexa.library.dtos.libro;


import jakarta.validation.constraints.NotBlank;
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
public class LibroRequest {

    @NotBlank
    private String titulo;

    @NotBlank
    private String isbn;

    @NotBlank
    private String edicion;

    private LocalDate fechaPublicacion;

    @NotBlank
    private String autor;
}
