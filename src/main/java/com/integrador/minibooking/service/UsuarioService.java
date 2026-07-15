package com.integrador.minibooking.service;

import com.integrador.minibooking.exception.ResourceNotFoundException;
import com.integrador.minibooking.model.Rol;
import com.integrador.minibooking.model.Usuario;
import com.integrador.minibooking.repository.RolRepository;
import com.integrador.minibooking.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con id " + id));
    }

    public Usuario guardar(Usuario usuario) {
        Integer rolId = usuario.getRol().getId();

        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el rol con id " + rolId));

        usuario.setRol(rol);

        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Integer id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con id " + id));

        Integer rolId = usuarioActualizado.getRol().getId();

        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el rol con id " + rolId));

        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setApellido(usuarioActualizado.getApellido());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setPassword(usuarioActualizado.getPassword());
        usuarioExistente.setRol(rol);

        return usuarioRepository.save(usuarioExistente);
    }

    public void eliminar(Integer id) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con id " + id));

        usuarioRepository.delete(usuarioExistente);
    }
}