package com.integrador.minibooking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "imagenes")
public class Imagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "url_imagen", nullable = false, length = 1024)
    private String urlImagen;

    public Imagen(Integer id, String titulo, String urlImagen) {
        this.id = id;
        this.titulo = titulo;
        this.urlImagen = urlImagen;
    }

    public Imagen(String titulo, String urlImagen) {
        this.titulo = titulo;
        this.urlImagen = urlImagen;
    }
}