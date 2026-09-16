package com.integrador.minibooking.service;

import com.integrador.minibooking.dto.LoginRequestDTO;
import com.integrador.minibooking.dto.RegistroRequestDTO;
import com.integrador.minibooking.dto.UsuarioResponseDTO;
import com.integrador.minibooking.exception.ConflictException;
import com.integrador.minibooking.exception.UnauthorizedException;
import com.integrador.minibooking.model.Rol;
import com.integrador.minibooking.model.Usuario;
import com.integrador.minibooking.repository.RolRepository;
import com.integrador.minibooking.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void registrarConDatosValidosGuardaUsuarioConPasswordHasheada() {
        RegistroRequestDTO request = crearRegistroRequest();
        Rol rolCliente = crearRolCliente();

        when(usuarioRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(rolRepository.findByNombre("CLIENTE")).thenReturn(Optional.of(rolCliente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setId(1);
            return usuario;
        });

        UsuarioResponseDTO resultado = authService.registrar(request);

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(usuarioCaptor.capture());
        Usuario usuarioGuardado = usuarioCaptor.getValue();

        assertEquals(1, resultado.getId());
        assertEquals(request.getNombre(), resultado.getNombre());
        assertEquals(request.getApellido(), resultado.getApellido());
        assertEquals(request.getEmail(), resultado.getEmail());
        assertEquals(rolCliente, resultado.getRol());
        assertNotEquals(request.getPassword(), usuarioGuardado.getPassword());
        assertTrue(new BCryptPasswordEncoder().matches(request.getPassword(), usuarioGuardado.getPassword()));
        assertEquals(rolCliente, usuarioGuardado.getRol());
    }

    @Test
    void registrarConEmailDuplicadoLanzaConflictException() {
        RegistroRequestDTO request = crearRegistroRequest();

        when(usuarioRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(ConflictException.class, () -> authService.registrar(request));

        verifyNoInteractions(rolRepository);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void loginConCredencialesValidasDevuelveUsuario() {
        LoginRequestDTO request = crearLoginRequest("123456");
        Usuario usuario = crearUsuarioConPasswordHasheada("123456");

        when(usuarioRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO resultado = authService.login(request);

        assertEquals(usuario.getId(), resultado.getId());
        assertEquals(usuario.getNombre(), resultado.getNombre());
        assertEquals(usuario.getApellido(), resultado.getApellido());
        assertEquals(usuario.getEmail(), resultado.getEmail());
        assertEquals(usuario.getRol(), resultado.getRol());
    }

    @Test
    void loginConPasswordIncorrectaLanzaUnauthorizedException() {
        LoginRequestDTO request = crearLoginRequest("password-incorrecta");
        Usuario usuario = crearUsuarioConPasswordHasheada("123456");

        when(usuarioRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(usuario));

        assertThrows(UnauthorizedException.class, () -> authService.login(request));
    }

    private RegistroRequestDTO crearRegistroRequest() {
        RegistroRequestDTO request = new RegistroRequestDTO();
        request.setNombre("Genaro");
        request.setApellido("Scrocca");
        request.setEmail("genaro@email.com");
        request.setPassword("123456");
        return request;
    }

    private LoginRequestDTO crearLoginRequest(String password) {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("genaro@email.com");
        request.setPassword(password);
        return request;
    }

    private Usuario crearUsuarioConPasswordHasheada(String password) {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Genaro");
        usuario.setApellido("Scrocca");
        usuario.setEmail("genaro@email.com");
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setRol(crearRolCliente());
        return usuario;
    }

    private Rol crearRolCliente() {
        Rol rol = new Rol();
        rol.setId(1);
        rol.setNombre("CLIENTE");
        return rol;
    }
}
