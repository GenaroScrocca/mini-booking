package com.integrador.minibooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ciudades")
public class Ciudad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre de la ciudad es obligatorio")
    @Size(max = 100, message = "El nombre de la ciudad no puede superar los 100 caracteres")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotBlank(message = "El país es obligatorio")
    @Size(max = 100, message = "El país no puede superar los 100 caracteres")
    @Column(name = "pais", nullable = false)
    private String pais;

    @NotBlank(message = "La provincia es obligatoria")
    @Size(max = 100, message = "La provincia no puede superar los 100 caracteres")
    @Column(name = "provincia", nullable = false)
    private String provincia;

    @OneToMany(mappedBy = "ciudad")
    @JsonIgnore
    private Set<Producto> productos;

    public Ciudad(Integer id, String nombre, String pais, String provincia) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.provincia = provincia;
    }

    public Ciudad(String nombre, String pais, String provincia) {
        this.nombre = nombre;
        this.pais = pais;
        this.provincia = provincia;
    }
}