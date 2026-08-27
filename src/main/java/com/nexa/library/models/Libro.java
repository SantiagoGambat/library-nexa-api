package com.nexa.library.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "LIB_LIBRO")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITULO", nullable = false, length = 250)
    private String titulo;

    @Column(name = "ISBN", nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(name = "EDICION", nullable = false, length = 100)
    private String edicion;

    @Column(name = "FECHA_PUBLICACION")
    private LocalDate fechaPublicacion;

    @Column(name = "AUTOR", nullable = false, length = 200)
    private String autor;

    @OneToMany(
        mappedBy = "libro",
        fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<Ejemplar> ejemplares = new ArrayList<>();
}
