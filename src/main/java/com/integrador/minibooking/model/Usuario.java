package com.integrador.minibooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre del usuario es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido del usuario es obligatorio")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
    @Column(name = "apellido", nullable = false)
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, max = 100, message = "La contraseña debe tener entre 4 y 100 caracteres")
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull(message = "El rol es obligatorio")
    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    public Usuario(Integer id, String nombre, String apellido, String email, String password, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    public Usuario(String nombre, String apellido, String email, String password, Rol rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }
}