package com.integrador.minibooking.service;

import com.integrador.minibooking.dto.LoginRequestDTO;
import com.integrador.minibooking.dto.RegistroRequestDTO;
import com.integrador.minibooking.dto.UsuarioResponseDTO;
import com.integrador.minibooking.exception.ConflictException;
import com.integrador.minibooking.exception.ResourceNotFoundException;
import com.integrador.minibooking.exception.UnauthorizedException;
import com.integrador.minibooking.model.Rol;
import com.integrador.minibooking.model.Usuario;
import com.integrador.minibooking.repository.RolRepository;
import com.integrador.minibooking.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final String ROL_CLIENTE = "CLIENTE";

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    public UsuarioResponseDTO registrar(RegistroRequestDTO registroRequestDTO) {
        if (usuarioRepository.existsByEmail(registroRequestDTO.getEmail())) {
            throw new ConflictException("Ya existe un usuario registrado con ese email");
        }

        Rol rolCliente = rolRepository.findByNombre(ROL_CLIENTE)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el rol CLIENTE"));

        Usuario usuario = new Usuario();
        usuario.setNombre(registroRequestDTO.getNombre());
        usuario.setApellido(registroRequestDTO.getApellido());
        usuario.setEmail(registroRequestDTO.getEmail());
        usuario.setPassword(passwordEncoder.encode(registroRequestDTO.getPassword()));
        usuario.setRol(rolCliente);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return UsuarioResponseDTO.fromEntity(usuarioGuardado);
    }

    public UsuarioResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Usuario usuario = usuarioRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas"));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), usuario.getPassword())) {
            throw new UnauthorizedException("Credenciales inválidas");
        }

        return UsuarioResponseDTO.fromEntity(usuario);
    }
}
