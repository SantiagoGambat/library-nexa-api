package com.nexa.library.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nexa.library.dtos.usuario.UsuarioRequest;
import com.nexa.library.dtos.usuario.UsuarioResponse;
import com.nexa.library.services.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(
        @Valid @RequestBody UsuarioRequest request
    ) {

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(usuarioService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {

        return ResponseEntity.ok(
            usuarioService.listar()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(
        @PathVariable Long id
    ) {

        return ResponseEntity.ok(
            usuarioService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizar(
        @PathVariable Long id,
        @Valid @RequestBody UsuarioRequest request
    ) {

        return ResponseEntity.ok(
            usuarioService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(
        @PathVariable Long id
    ) {

        usuarioService.eliminar(id);
    }
}
