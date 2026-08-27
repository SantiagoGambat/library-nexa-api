package com.nexa.library.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.nexa.library.models.enums.EstadoPrestamo;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "LIB_PRESTAMO")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FECHA_PRESTAMO", nullable = false)
    private LocalDateTime fechaPrestamo;

    @Column(name = "FECHA_DEVOLUCION", nullable = false)
    private LocalDateTime fechaDevolucion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EJEMPLAR_ID", nullable = false)
    private Ejemplar ejemplar;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_PRESTAMO", nullable = false, length = 20)
    private EstadoPrestamo estadoPrestamo;
}
