package com.nexa.library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexa.library.dtos.prestamo.PrestamoRequest;
import com.nexa.library.dtos.prestamo.PrestamoResponse;
import com.nexa.library.exceptions.RecursoNoEncontradoException;
import com.nexa.library.exceptions.ReglaNegocioException;
import com.nexa.library.mappers.PrestamoMapper;
import com.nexa.library.models.Ejemplar;
import com.nexa.library.models.Prestamo;
import com.nexa.library.models.Usuario;
import com.nexa.library.models.enums.EstadoPrestamo;
import com.nexa.library.repositroy.EjemplarRepository;
import com.nexa.library.repositroy.PrestamoRepository;
import com.nexa.library.repositroy.UsuarioRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PrestamoServiceImpl implements PrestamoService {

        private final PrestamoRepository prestamoRepository;
        private final UsuarioRepository usuarioRepository;
        private final EjemplarRepository ejemplarRepository;

        @Override
        public PrestamoResponse crear(PrestamoRequest request) {

                // 1. Buscar usuario
                Usuario usuario = usuarioRepository.findById(
                                request.getUsuarioId()).orElseThrow(
                                                () -> new RecursoNoEncontradoException(
                                                                "Usuario no encontrado: " + request.getUsuarioId()));

                LocalDateTime fechaActual = LocalDateTime.now();

                LocalDateTime fechaPrestamo = request.getFechaPrestamo();
                LocalDateTime fechaDevolucion = request.getFechaDevolucion();

                LocalDate hoy = fechaActual.toLocalDate();
                LocalDate diaPrestamo = fechaPrestamo.toLocalDate();

                /*
                 * 1. Validar que la fecha del préstamo
                 * no sea anterior al día actual.
                 *
                 * Se compara únicamente la fecha,
                 * ignorando hora, minutos y segundos.
                 */
                if (diaPrestamo.isBefore(hoy)) {
                        throw new ReglaNegocioException(
                                        "La fecha del préstamo no puede ser anterior al día actual");
                }


                // 3. Validar fecha de devolución
                if (!fechaDevolucion.isAfter(fechaPrestamo)) {
                        throw new ReglaNegocioException(
                                        "La fecha de devolución debe ser posterior a la fecha del préstamo");
                }

                // 4. Buscar el ejemplar seleccionado
                Ejemplar ejemplar = ejemplarRepository.findById(
                                request.getEjemplarId()).orElseThrow(
                                                () -> new RecursoNoEncontradoException(
                                                                "Ejemplar no encontrado: " + request.getEjemplarId()));

                // 5. Validar que el ejemplar pertenezca al libro seleccionado
                if (!ejemplar.getLibro().getIsbn().equals(request.getIsbn())) {
                        throw new ReglaNegocioException(
                                        "El ejemplar seleccionado no pertenece al libro indicado");
                }

                /*
                 * 6. Validar disponibilidad del ejemplar
                 *
                 * El ejemplar no puede estar asignado a otro préstamo
                 * que esté activo o programado.
                 */
                boolean tienePrestamoActivoDelLibro = prestamoRepository
                                .existsByUsuarioIdAndEjemplarLibroIsbnAndEstadoPrestamoIn(
                                                usuario.getId(),
                                                request.getIsbn(),
                                                List.of(
                                                                EstadoPrestamo.PROGRAMADO,
                                                                EstadoPrestamo.ACTIVO,
                                                                EstadoPrestamo.VENCIDO));

                if (tienePrestamoActivoDelLibro) {
                        throw new ReglaNegocioException(
                                        "El ejemplar seleccionado no está disponible ya que ya tienes uno de este Libro");
                }

                /*
                 * 7. Regla de negocio:
                 *
                 * Un usuario puede tener varios préstamos,
                 * pero no puede tener más de un préstamo del mismo libro.
                 *
                 * Solo consideramos préstamos PROGRAMADOS y ACTIVOS.
                 *
                 * Un préstamo VENCIDO ya no bloquea al usuario.
                 */
                boolean tienePrestamoDelLibro = prestamoRepository
                                .existsByUsuarioIdAndEjemplarLibroIsbnAndEstadoPrestamoIn(
                                                usuario.getId(),
                                                request.getIsbn(),
                                                List.of(
                                                                EstadoPrestamo.PROGRAMADO,
                                                                EstadoPrestamo.ACTIVO));

                if (tienePrestamoDelLibro) {
                        throw new ReglaNegocioException(
                                        "El usuario ya tiene un préstamo activo o programado de este libro");
                }

                // 8. Determinar el estado del préstamo
                EstadoPrestamo estado;

                if (fechaPrestamo.toLocalDate()
                                .isEqual(fechaActual.toLocalDate())) {

                        estado = EstadoPrestamo.ACTIVO;

                } else if (fechaPrestamo.toLocalDate()
                                .isAfter(fechaActual.toLocalDate())) {

                        estado = EstadoPrestamo.PROGRAMADO;

                } else {

                        throw new ReglaNegocioException(
                                        "La fecha del préstamo no puede ser anterior a hoy");
                }

                // 9. Crear préstamo
                Prestamo prestamo = Prestamo.builder()
                                .fechaPrestamo(fechaPrestamo)
                                .fechaDevolucion(fechaDevolucion)
                                .usuario(usuario)
                                .ejemplar(ejemplar)
                                .estadoPrestamo(estado)
                                .build();

                // 10. Guardar
                return convertirRespuesta(
                                prestamoRepository.save(prestamo));
        }

        @Override
        @Transactional(readOnly = true)
        public List<PrestamoResponse> listarPorUsuario(
                        Long usuarioId) {

                if (!usuarioRepository.existsById(usuarioId)) {
                        throw new RecursoNoEncontradoException(
                                        "Usuario no encontrado: " + usuarioId);
                }

                return prestamoRepository
                                .findByUsuarioId(usuarioId)
                                .stream()
                                .map(this::convertirRespuesta)
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public List<PrestamoResponse> listarPorLibro(
                        Long libroId) {

                return prestamoRepository
                                .findByEjemplarLibroId(libroId)
                                .stream()
                                .map(this::convertirRespuesta)
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public List<PrestamoResponse> listarPorIsbn(
                        String isbn) {

                return prestamoRepository
                                .findByEjemplarLibroIsbn(isbn)
                                .stream()
                                .map(this::convertirRespuesta)
                                .toList();
        }

        private Ejemplar buscarEjemplarDisponible(
                        String isbn) {

                List<Ejemplar> ejemplares = ejemplarRepository.findByLibroIsbn(isbn);

                return ejemplares.stream()
                                .filter(ejemplar -> !prestamoRepository
                                                .existsByEjemplarIdAndEstadoPrestamo(
                                                                ejemplar.getId(),
                                                                EstadoPrestamo.ACTIVO))
                                .findFirst()
                                .orElseThrow(() -> new ReglaNegocioException(
                                                "No hay ejemplares disponibles "
                                                                + "para el ISBN: " + isbn));
        }

        private EstadoPrestamo calcularEstado(
                        LocalDateTime fechaPrestamo,
                        LocalDateTime fechaDevolucion) {

                LocalDateTime fechaActual = LocalDateTime.now();

                if (fechaActual.isBefore(fechaPrestamo)) {
                        return EstadoPrestamo.PROGRAMADO;
                }

                if (fechaActual.isAfter(fechaDevolucion)) {
                        return EstadoPrestamo.VENCIDO;
                }

                return EstadoPrestamo.ACTIVO;
        }

        private PrestamoResponse convertirRespuesta(
                        Prestamo prestamo) {

                EstadoPrestamo estadoActual = calcularEstado(
                                prestamo.getFechaPrestamo(),
                                prestamo.getFechaDevolucion());

                return new PrestamoResponse(
                                prestamo.getId(),
                                prestamo.getFechaPrestamo(),
                                prestamo.getFechaDevolucion(),
                                prestamo.getUsuario().getId(),
                                prestamo.getEjemplar().getId(),
                                prestamo.getEjemplar().getCodigoInventario(),
                                prestamo.getEjemplar().getLibro().getIsbn(),
                                estadoActual);
        }

        @Override
        public List<PrestamoResponse> listar() {
                return prestamoRepository.findAll()
                                .stream()
                                .map(PrestamoMapper.INSTANCE::toResponse)
                                .toList();
        }
}
