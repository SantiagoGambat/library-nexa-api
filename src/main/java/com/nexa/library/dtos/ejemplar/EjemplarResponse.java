package com.nexa.library.dtos.ejemplar;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EjemplarResponse {

    private Long id;

    private String codigoInventario;

    private Long libroId;

    private String isbn;

    private String titulo;

    private boolean disponible;
}
