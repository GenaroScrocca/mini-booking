package com.integrador.minibooking.dto;

import com.integrador.minibooking.model.Rol;
import com.integrador.minibooking.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Integer id;
    private String nombre;
    private String apellido;
    private String email;
    private Rol rol;

    public static UsuarioResponseDTO fromEntity(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRol()
        );
    }
}
