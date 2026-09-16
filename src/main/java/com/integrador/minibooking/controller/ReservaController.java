package com.integrador.minibooking.controller;

import com.integrador.minibooking.dto.ReservaResponseDTO;
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
    public List<ReservaResponseDTO> listarTodas() {
        return reservaService.listarTodas()
                .stream()
                .map(ReservaResponseDTO::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> buscarPorId(@PathVariable Integer id) {
        Reserva reserva = reservaService.buscarPorId(id);
        return ResponseEntity.ok(ReservaResponseDTO.fromEntity(reserva));
    }

    @PostMapping
    public ReservaResponseDTO guardar(@Valid @RequestBody Reserva reserva) {
        Reserva reservaGuardada = reservaService.guardar(reserva);
        return ReservaResponseDTO.fromEntity(reservaGuardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody Reserva reserva) {
        Reserva reservaActualizada = reservaService.actualizar(id, reserva);
        return ResponseEntity.ok(ReservaResponseDTO.fromEntity(reservaActualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
