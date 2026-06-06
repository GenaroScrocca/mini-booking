package com.integrador.minibooking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "descripciones")
public class Descripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "descripcion", nullable = false, length = 2000)
    private String descripcion;

    public Descripcion(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Descripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}