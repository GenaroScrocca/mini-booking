package com.integrador.minibooking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "caracteristicas")
public class Caracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "url_icono", nullable = false)
    private String urlIcono;

    public Caracteristica(Integer id, String nombre, String urlIcono) {
        this.id = id;
        this.nombre = nombre;
        this.urlIcono = urlIcono;
    }

    public Caracteristica(String nombre, String urlIcono) {
        this.nombre = nombre;
        this.urlIcono = urlIcono;
    }
}