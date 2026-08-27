package com.nexa.library.dtos.prestamo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrestamoRequest {

    @NotNull
    private Long usuarioId;

    @NotBlank
    private String isbn;

    @NotNull
    private LocalDateTime fechaPrestamo;

    @NotNull
    private Long ejemplarId;

    @NotNull
    private LocalDateTime fechaDevolucion;
}
