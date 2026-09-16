package com.integrador.minibooking.service;

import com.integrador.minibooking.exception.BadRequestException;
import com.integrador.minibooking.exception.ResourceNotFoundException;
import com.integrador.minibooking.model.Rol;
import com.integrador.minibooking.model.Usuario;
import com.integrador.minibooking.repository.RolRepository;
import com.integrador.minibooking.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void guardarConRolSinIdLanzaBadRequestException() {
        Usuario usuario = new Usuario();
        usuario.setRol(new Rol());

        assertThrows(BadRequestException.class, () -> usuarioService.guardar(usuario));

        verifyNoInteractions(rolRepository);
        verify(usuarioRepository, never()).save(usuario);
    }

    @Test
    void guardarConRolInexistenteLanzaResourceNotFoundException() {
        Usuario usuario = new Usuario();
        Rol rol = new Rol();
        rol.setId(99);
        usuario.setRol(rol);

        when(rolRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> usuarioService.guardar(usuario)
        );

        assertEquals("No se encontró el rol con id 99", exception.getMessage());
        verify(usuarioRepository, never()).save(usuario);
    }
}
