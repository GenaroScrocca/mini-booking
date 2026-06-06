package com.integrador.minibooking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "politicas")
public class Politica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipo_politica")
    private String tipoPolitica;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion", length = 1000)
    private String descripcion;

    public Politica(Integer id, String tipoPolitica, String titulo, String descripcion) {
        this.id = id;
        this.tipoPolitica = tipoPolitica;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public Politica(String tipoPolitica, String titulo, String descripcion) {
        this.tipoPolitica = tipoPolitica;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }
}