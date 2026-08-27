package com.nexa.library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexa.library.dtos.usuario.UsuarioRequest;
import com.nexa.library.dtos.usuario.UsuarioResponse;
import com.nexa.library.exceptions.RecursoNoEncontradoException;
import com.nexa.library.exceptions.ReglaNegocioException;
import com.nexa.library.mappers.UsuarioMapper;
import com.nexa.library.models.Usuario;
import com.nexa.library.repositroy.PrestamoRepository;
import com.nexa.library.repositroy.UsuarioRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;

    @Override
    public UsuarioResponse crear(UsuarioRequest request) {

        if (usuarioRepository.existsByEmailIgnoreCase(
                request.getEmail())) {
            throw new ReglaNegocioException(
                    "Ya existe un usuario con el email: "
                            + request.getEmail());
        }

        Usuario usuario = UsuarioMapper.INSTANCE.toEntity(request);

        return UsuarioMapper.INSTANCE.toResponse(
                usuarioRepository.save(usuario));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {

        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioMapper.INSTANCE::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {

        return UsuarioMapper.INSTANCE.toResponse(
                obtenerUsuario(id));
    }

    @Override
    public UsuarioResponse actualizar(
            Long id,
            UsuarioRequest request) {

        Usuario usuario = obtenerUsuario(id);

        if (!usuario.getEmail().equalsIgnoreCase(
                request.getEmail())
                && usuarioRepository.existsByEmailIgnoreCase(
                        request.getEmail())) {

            throw new ReglaNegocioException(
                    "Ya existe un usuario con el email: "
                            + request.getEmail());
        }

        UsuarioMapper.INSTANCE.updateEntity(request, usuario);

        return UsuarioMapper.INSTANCE.toResponse(usuario);
    }

    @Override
    public void eliminar(Long id) {

        Usuario usuario = obtenerUsuario(id);

        boolean tienePrestamos = prestamoRepository.existsByUsuarioId(id);

        if (tienePrestamos) {
            throw new ReglaNegocioException(
                    "No se puede eliminar el usuario porque tiene préstamos asociados.");
        }

        usuarioRepository.delete(usuario);
    }

    private Usuario obtenerUsuario(Long id) {

        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado: " + id));
    }
}
