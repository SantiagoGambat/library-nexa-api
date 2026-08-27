package com.nexa.library.repositroy;


import org.springframework.data.jpa.repository.JpaRepository;

import com.nexa.library.models.Usuario;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {

    boolean existsByEmailIgnoreCase(String email);
}
