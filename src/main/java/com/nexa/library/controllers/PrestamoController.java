package com.nexa.library.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nexa.library.dtos.prestamo.PrestamoRequest;
import com.nexa.library.dtos.prestamo.PrestamoResponse;
import com.nexa.library.services.PrestamoService;

import java.util.List;

@RestController
@RequestMapping("/prestamos")
@RequiredArgsConstructor
public class PrestamoController {

    private final PrestamoService prestamoService;

    @PostMapping
    public ResponseEntity<PrestamoResponse> crear(
        @Valid @RequestBody PrestamoRequest request
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(prestamoService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<PrestamoResponse>> listar() {
        return ResponseEntity.ok(
            prestamoService.listar()
        );
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PrestamoResponse>> listarPorUsuario(
        @PathVariable Long usuarioId
    ) {
        return ResponseEntity.ok(
            prestamoService.listarPorUsuario(usuarioId)
        );
    }

    @GetMapping("/libro/{libroId}")
    public ResponseEntity<List<PrestamoResponse>> listarPorLibro(
        @PathVariable Long libroId
    ) {
        return ResponseEntity.ok(
            prestamoService.listarPorLibro(libroId)
        );
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<List<PrestamoResponse>> listarPorIsbn(
        @PathVariable String isbn
    ) {
        return ResponseEntity.ok(
            prestamoService.listarPorIsbn(isbn)
        );
    }
}
