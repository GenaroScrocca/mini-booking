package com.integrador.minibooking.service;

import com.integrador.minibooking.exception.ResourceNotFoundException;
import com.integrador.minibooking.model.Producto;
import com.integrador.minibooking.model.Reserva;
import com.integrador.minibooking.model.Usuario;
import com.integrador.minibooking.repository.ProductoRepository;
import com.integrador.minibooking.repository.ReservaRepository;
import com.integrador.minibooking.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

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

    public Reserva buscarPorId(Integer id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la reserva con id " + id));
    }

    public Reserva guardar(Reserva reserva) {
        Integer productoId = reserva.getProducto().getId();
        Integer usuarioId = reserva.getUsuario().getId();

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con id " + productoId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con id " + usuarioId));

        reserva.setProducto(producto);
        reserva.setUsuario(usuario);

        return reservaRepository.save(reserva);
    }

    public Reserva actualizar(Integer id, Reserva reservaActualizada) {
        Reserva reservaExistente = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la reserva con id " + id));

        Integer productoId = reservaActualizada.getProducto().getId();
        Integer usuarioId = reservaActualizada.getUsuario().getId();

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con id " + productoId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con id " + usuarioId));

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
}