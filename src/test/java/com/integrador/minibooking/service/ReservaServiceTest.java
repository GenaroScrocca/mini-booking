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
