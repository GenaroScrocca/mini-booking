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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public ReservaService(
            ReservaRepository reservaRepository,
            ProductoRepository productoRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.reservaRepository = reservaRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    public List<Reserva> buscarPorUsuario(Integer usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId);
    }

    public Reserva buscarPorId(Integer id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la reserva con id " + id));
    }

    public Reserva guardar(Reserva reserva) {
        validarFechas(reserva.getFechaInicio(), reserva.getFechaFin());

        Producto producto = buscarProductoDeReserva(reserva);
        Usuario usuario = buscarUsuarioDeReserva(reserva);

        validarDisponibilidadProducto(producto.getId(), reserva.getFechaInicio(), reserva.getFechaFin());

        reserva.setProducto(producto);
        reserva.setUsuario(usuario);

        return reservaRepository.save(reserva);
    }

    public Reserva actualizar(Integer id, Reserva reservaActualizada) {
        Reserva reservaExistente = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la reserva con id " + id));

        validarFechas(reservaActualizada.getFechaInicio(), reservaActualizada.getFechaFin());

        Producto producto = buscarProductoDeReserva(reservaActualizada);
        Usuario usuario = buscarUsuarioDeReserva(reservaActualizada);

        validarDisponibilidadProductoParaActualizacion(
                producto.getId(),
                id,
                reservaActualizada.getFechaInicio(),
                reservaActualizada.getFechaFin()
        );

        reservaExistente.setFechaInicio(reservaActualizada.getFechaInicio());
        reservaExistente.setFechaFin(reservaActualizada.getFechaFin());
        reservaExistente.setProducto(producto);
        reservaExistente.setUsuario(usuario);

        return reservaRepository.save(reservaExistente);
    }

    public void eliminar(Integer id) {
        Reserva reservaExistente = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la reserva con id " + id));

        reservaRepository.delete(reservaExistente);
    }

    private void validarFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new BadRequestException("Las fechas de la reserva son obligatorias");
        }

        if (!fechaFin.isAfter(fechaInicio)) {
            throw new BadRequestException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
    }

    private Producto buscarProductoDeReserva(Reserva reserva) {
        if (reserva.getProducto() == null || reserva.getProducto().getId() == null) {
            throw new BadRequestException("El id del producto es obligatorio para la reserva");
        }

        Integer productoId = reserva.getProducto().getId();

        return productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con id " + productoId));
    }

    private Usuario buscarUsuarioDeReserva(Reserva reserva) {
        if (reserva.getUsuario() == null || reserva.getUsuario().getId() == null) {
            throw new BadRequestException("El id del usuario es obligatorio para la reserva");
        }

        Integer usuarioId = reserva.getUsuario().getId();

        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con id " + usuarioId));
    }

    private void validarDisponibilidadProducto(Integer productoId, LocalDate fechaInicio, LocalDate fechaFin) {
        boolean existeReservaSolapada = reservaRepository.existeReservaSolapada(
                productoId,
                fechaInicio,
                fechaFin
        );

        if (existeReservaSolapada) {
            throw new ConflictException("El producto ya tiene una reserva en ese rango de fechas");
        }
    }

    private void validarDisponibilidadProductoParaActualizacion(
            Integer productoId,
            Integer reservaId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        boolean existeReservaSolapada = reservaRepository.existeReservaSolapadaExcluyendoReserva(
                productoId,
                reservaId,
                fechaInicio,
                fechaFin
        );

        if (existeReservaSolapada) {
            throw new ConflictException("El producto ya tiene una reserva en ese rango de fechas");
        }
    }
}
