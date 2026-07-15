package com.integrador.minibooking.controller;

import com.integrador.minibooking.model.Reserva;
import com.integrador.minibooking.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public List<Reserva> listarTodas() {
        return reservaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> buscarPorId(@PathVariable Integer id) {
        Reserva reserva = reservaService.buscarPorId(id);
        return ResponseEntity.ok(reserva);
    }

    @PostMapping
    public Reserva guardar(@Valid @RequestBody Reserva reserva) {
        return reservaService.guardar(reserva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> actualizar(@PathVariable Integer id, @Valid @RequestBody Reserva reserva) {
        Reserva reservaActualizada = reservaService.actualizar(id, reserva);
        return ResponseEntity.ok(reservaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}