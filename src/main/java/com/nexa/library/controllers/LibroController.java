package com.nexa.library.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nexa.library.dtos.ejemplar.EjemplarResponse;
import com.nexa.library.dtos.libro.LibroRequest;
import com.nexa.library.dtos.libro.LibroResponse;
import com.nexa.library.services.LibroService;

import java.util.List;

@RestController
@RequestMapping("/libros")
@RequiredArgsConstructor
public class LibroController {

    private final LibroService libroService;

    @PostMapping
    public ResponseEntity<LibroResponse> crear(
            @Valid @RequestBody LibroRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(libroService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<LibroResponse>> listar() {

        return ResponseEntity.ok(
                libroService.listar());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<LibroResponse>> listarDisponibles() {

        return ResponseEntity.ok(
                libroService.listarConEjemplarDisponible());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                libroService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody LibroRequest request) {

        return ResponseEntity.ok(
                libroService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(
            @PathVariable Long id) {

        libroService.eliminar(id);
    }

    @GetMapping("/isbn/{isbn}/ejemplares/disponibles")
    public ResponseEntity<List<EjemplarResponse>> listarEjemplaresDisponibles(
            @PathVariable String isbn) {

        return ResponseEntity.ok(
                libroService.listarEjemplaresDisponibles(isbn));
    }
}
