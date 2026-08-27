package com.nexa.library.dtos.ejemplar;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EjemplarRequest {

    @NotNull(message = "El libro es obligatorio")
    private Long libroId;

    @NotBlank(message = "El código de inventario es obligatorio")
    private String codigoInventario;
}