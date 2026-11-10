package com.integrador.minibooking.service;

import com.integrador.minibooking.exception.BadRequestException;
import com.integrador.minibooking.exception.ConflictException;
import com.integrador.minibooking.exception.ResourceNotFoundException;
import com.integrador.minibooking.model.Producto;
import com.integrador.minibooking.model.Reserva;
import com.integrador.minibooking.model.Usuario;
import com.integrador.minibooking.repository.ProductoRepository;
import com.integrador.minibooking.repository.ReservaRepository;
import com.integrador.minibooking.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ReservaService reservaService;

    @Test
    void guardarConFechaFinNoPosteriorAFechaInicioLanzaBadRequestException() {
        Reserva reserva = crearReserva(1, 1, LocalDate.now().plusDays(5), LocalDate.now().plusDays(5));

        assertThrows(BadRequestException.class, () -> reservaService.guardar(reserva));

        verifyNoInteractions(productoRepository, usuarioRepository, reservaRepository);
    }

    @Test
    void guardarConReservaSolapadaLanzaConflictException() {
        LocalDate fechaInicio = LocalDate.now().plusDays(5);
        LocalDate fechaFin = LocalDate.now().plusDays(8);
        Reserva reserva = crearReserva(1, 1, fechaInicio, fechaFin);
        Producto producto = crearProducto(1);
        Usuario usuario = crearUsuario(1);

        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(reservaRepository.existeReservaSolapada(1, fechaInicio, fechaFin)).thenReturn(true);

        assertThrows(ConflictException.class, () -> reservaService.guardar(reserva));

        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    void guardarConReservaDisponibleGuardaCorrectamente() {
        LocalDate fechaInicio = LocalDate.now().plusDays(5);
        LocalDate fechaFin = LocalDate.now().plusDays(8);
        Reserva reserva = crearReserva(1, 1, fechaInicio, fechaFin);
        Producto producto = crearProducto(1);
        Usuario usuario = crearUsuario(1);

        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(reservaRepository.existeReservaSolapada(1, fechaInicio, fechaFin)).thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reserva resultado = reservaService.guardar(reserva);

        assertSame(reserva, resultado);
        assertSame(producto, resultado.getProducto());
        assertSame(usuario, resultado.getUsuario());
        verify(reservaRepository).save(reserva);
    }

    @Test
    void guardarConProductoSinIdLanzaBadRequestException() {
        Reserva reserva = crearReserva(null, 1, LocalDate.now().plusDays(5), LocalDate.now().plusDays(8));

        assertThrows(BadRequestException.class, () -> reservaService.guardar(reserva));

        verifyNoInteractions(productoRepository, usuarioRepository, reservaRepository);
    }

    @Test
    void guardarConProductoInexistenteLanzaResourceNotFoundException() {
        Reserva reserva = crearReserva(99, 1, LocalDate.now().plusDays(5), LocalDate.now().plusDays(8));

        when(productoRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> reservaService.guardar(reserva)
        );

        assertEquals("No se encontró el producto con id 99", exception.getMessage());
        verifyNoInteractions(usuarioRepository);
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    void actualizarReservaValidaGuardaLosCambios() {
        Integer reservaId = 10;
        LocalDate nuevaFechaInicio = LocalDate.now().plusDays(12);
        LocalDate nuevaFechaFin = LocalDate.now().plusDays(16);
        Reserva reservaExistente = crearReserva(
                1,
                1,
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(8)
        );
        reservaExistente.setId(reservaId);
        Reserva datosActualizados = crearReserva(2, 3, nuevaFechaInicio, nuevaFechaFin);
        Producto producto = crearProducto(2);
        Usuario usuario = crearUsuario(3);

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reservaExistente));
        when(productoRepository.findById(2)).thenReturn(Optional.of(producto));
        when(usuarioRepository.findById(3)).thenReturn(Optional.of(usuario));
        when(reservaRepository.existeReservaSolapadaExcluyendoReserva(
                2,
                reservaId,
                nuevaFechaInicio,
                nuevaFechaFin
        )).thenReturn(false);
        when(reservaRepository.save(reservaExistente)).thenReturn(reservaExistente);

        Reserva resultado = reservaService.actualizar(reservaId, datosActualizados);

        assertSame(reservaExistente, resultado);
        assertEquals(nuevaFechaInicio, resultado.getFechaInicio());
        assertEquals(nuevaFechaFin, resultado.getFechaFin());
        assertSame(producto, resultado.getProducto());
        assertSame(usuario, resultado.getUsuario());
        verify(reservaRepository).save(reservaExistente);
    }

    @Test
    void actualizarConReservaSolapadaLanzaConflictException() {
        Integer reservaId = 10;
        LocalDate nuevaFechaInicio = LocalDate.now().plusDays(12);
        LocalDate nuevaFechaFin = LocalDate.now().plusDays(16);
        Reserva reservaExistente = crearReserva(1, 1, nuevaFechaInicio, nuevaFechaFin);
        reservaExistente.setId(reservaId);
        Reserva datosActualizados = crearReserva(1, 1, nuevaFechaInicio, nuevaFechaFin);
        Producto producto = crearProducto(1);
        Usuario usuario = crearUsuario(1);

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reservaExistente));
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(reservaRepository.existeReservaSolapadaExcluyendoReserva(
                1,
                reservaId,
                nuevaFechaInicio,
                nuevaFechaFin
        )).thenReturn(true);

        assertThrows(ConflictException.class, () -> reservaService.actualizar(reservaId, datosActualizados));

        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    void actualizarExcluyeLaPropiaReservaDeLaValidacionDeSolapamiento() {
        Integer reservaId = 10;
        LocalDate fechaInicio = LocalDate.now().plusDays(12);
        LocalDate fechaFin = LocalDate.now().plusDays(16);
        Reserva reservaExistente = crearReserva(1, 1, fechaInicio, fechaFin);
        reservaExistente.setId(reservaId);
        Reserva mismosDatos = crearReserva(1, 1, fechaInicio, fechaFin);
        Producto producto = crearProducto(1);
        Usuario usuario = crearUsuario(1);

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reservaExistente));
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(reservaRepository.existeReservaSolapadaExcluyendoReserva(
                1,
                reservaId,
                fechaInicio,
                fechaFin
        )).thenReturn(false);
        when(reservaRepository.save(reservaExistente)).thenReturn(reservaExistente);

        reservaService.actualizar(reservaId, mismosDatos);

        verify(reservaRepository).existeReservaSolapadaExcluyendoReserva(
                1,
                reservaId,
                fechaInicio,
                fechaFin
        );
        verify(reservaRepository).save(reservaExistente);
    }

    @Test
    void actualizarConFechaFinNoPosteriorLanzaBadRequestException() {
        Integer reservaId = 10;
        LocalDate fecha = LocalDate.now().plusDays(12);
        Reserva reservaExistente = crearReserva(
                1,
                1,
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(8)
        );
        reservaExistente.setId(reservaId);
        Reserva datosInvalidos = crearReserva(1, 1, fecha, fecha);

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reservaExistente));

        assertThrows(BadRequestException.class, () -> reservaService.actualizar(reservaId, datosInvalidos));

        verifyNoInteractions(productoRepository, usuarioRepository);
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    void eliminarReservaExistenteLaElimina() {
        Integer reservaId = 10;
        Reserva reservaExistente = crearReserva(
                1,
                1,
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(8)
        );
        reservaExistente.setId(reservaId);

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reservaExistente));

        reservaService.eliminar(reservaId);

        verify(reservaRepository).delete(reservaExistente);
    }

    private Reserva crearReserva(Integer productoId, Integer usuarioId, LocalDate fechaInicio, LocalDate fechaFin) {
        Reserva reserva = new Reserva();
        reserva.setFechaInicio(fechaInicio);
        reserva.setFechaFin(fechaFin);
        reserva.setProducto(crearProducto(productoId));
        reserva.setUsuario(crearUsuario(usuarioId));
        return reserva;
    }

    private Producto crearProducto(Integer id) {
        Producto producto = new Producto();
        producto.setId(id);
        return producto;
    }

    private Usuario crearUsuario(Integer id) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        return usuario;
    }
}
