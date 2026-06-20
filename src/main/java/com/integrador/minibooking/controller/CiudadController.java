package com.integrador.minibooking.controller;

import com.integrador.minibooking.model.Ciudad;
import com.integrador.minibooking.service.CiudadService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ciudades")
public class CiudadController {

    private final CiudadService ciudadService;

    public CiudadController(CiudadService ciudadService) {
        this.ciudadService = ciudadService;
    }

    @GetMapping
    public List<Ciudad> listarTodas() {
        return ciudadService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ciudad> buscarPorId(@PathVariable Integer id) {
        Ciudad ciudad = ciudadService.buscarPorId(id);
        return ResponseEntity.ok(ciudad);
    }

    @PostMapping
    public Ciudad guardar(@Valid @RequestBody Ciudad ciudad) {
        return ciudadService.guardar(ciudad);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ciudad> actualizar(@PathVariable Integer id, @Valid @RequestBody Ciudad ciudad) {
        Ciudad ciudadActualizada = ciudadService.actualizar(id, ciudad);
        return ResponseEntity.ok(ciudadActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        ciudadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}