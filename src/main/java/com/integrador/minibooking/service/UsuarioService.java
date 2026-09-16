package com.integrador.minibooking.service;

import com.integrador.minibooking.exception.BadRequestException;
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
        Rol rol = buscarRolDelUsuario(usuario);

        usuario.setRol(rol);

        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Integer id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con id " + id));

        Rol rol = buscarRolDelUsuario(usuarioActualizado);

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

    private Rol buscarRolDelUsuario(Usuario usuario) {
        if (usuario.getRol() == null || usuario.getRol().getId() == null) {
            throw new BadRequestException("El id del rol es obligatorio para el usuario");
        }

        Integer rolId = usuario.getRol().getId();

        return rolRepository.findById(rolId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el rol con id " + rolId));
    }
}
