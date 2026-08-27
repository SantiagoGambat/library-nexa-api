package com.nexa.library.controllers;

import com.nexa.library.dtos.ejemplar.EjemplarRequest;
import com.nexa.library.dtos.ejemplar.EjemplarResponse;
import com.nexa.library.services.EjemplarService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ejemplares")
@RequiredArgsConstructor
public class EjemplarController {

    private final EjemplarService ejemplarService;

    @PostMapping
    public ResponseEntity<EjemplarResponse> crear(
        @Valid @RequestBody EjemplarRequest request
    ) {

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ejemplarService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<EjemplarResponse>> listar() {

        return ResponseEntity.ok(
            ejemplarService.listar()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EjemplarResponse> buscarPorId(
        @PathVariable Long id
    ) {

        return ResponseEntity.ok(
            ejemplarService.buscarPorId(id)
        );
    }

    @GetMapping("/libro/{libroId}")
    public ResponseEntity<List<EjemplarResponse>> listarPorLibro(
        @PathVariable Long libroId
    ) {

        return ResponseEntity.ok(
            ejemplarService.listarPorLibro(libroId)
        );
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<EjemplarResponse>> listarDisponibles(
        @RequestParam String isbn
    ) {

        return ResponseEntity.ok(
            ejemplarService.listarDisponiblesPorIsbn(isbn)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<EjemplarResponse> actualizar(
        @PathVariable Long id,
        @Valid @RequestBody EjemplarRequest request
    ) {

        return ResponseEntity.ok(
            ejemplarService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
        @PathVariable Long id
    ) {

        ejemplarService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
