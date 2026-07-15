package com.integrador.minibooking.controller;

import com.integrador.minibooking.model.Rol;
import com.integrador.minibooking.service.RolService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public List<Rol> listarTodos() {
        return rolService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rol> buscarPorId(@PathVariable Integer id) {
        Rol rol = rolService.buscarPorId(id);
        return ResponseEntity.ok(rol);
    }

    @PostMapping
    public Rol guardar(@Valid @RequestBody Rol rol) {
        return rolService.guardar(rol);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rol> actualizar(@PathVariable Integer id, @Valid @RequestBody Rol rol) {
        Rol rolActualizado = rolService.actualizar(id, rol);
        return ResponseEntity.ok(rolActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        rolService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}