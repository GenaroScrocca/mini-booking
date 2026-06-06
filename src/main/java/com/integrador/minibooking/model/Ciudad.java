package com.integrador.minibooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "pais", nullable = false)
    private String pais;

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