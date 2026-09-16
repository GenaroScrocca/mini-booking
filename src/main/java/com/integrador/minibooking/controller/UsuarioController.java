package com.integrador.minibooking.controller;

import com.integrador.minibooking.dto.UsuarioResponseDTO;
import com.integrador.minibooking.model.Usuario;
import com.integrador.minibooking.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioService.listarTodos()
                .stream()
                .map(UsuarioResponseDTO::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Integer id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(UsuarioResponseDTO.fromEntity(usuario));
    }

    @PostMapping
    public UsuarioResponseDTO guardar(@Valid @RequestBody Usuario usuario) {
        Usuario usuarioGuardado = usuarioService.guardar(usuario);
        return UsuarioResponseDTO.fromEntity(usuarioGuardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody Usuario usuario) {
        Usuario usuarioActualizado = usuarioService.actualizar(id, usuario);
        return ResponseEntity.ok(UsuarioResponseDTO.fromEntity(usuarioActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
