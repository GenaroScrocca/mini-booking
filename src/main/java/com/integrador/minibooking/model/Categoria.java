package com.integrador.minibooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "El título de la categoría es obligatorio")
    @Size(max = 100, message = "El título no puede superar los 100 caracteres")
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @NotBlank(message = "La descripción de la categoría es obligatoria")
    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @NotBlank(message = "La URL de la imagen es obligatoria")
    @Size(max = 1024, message = "La URL de la imagen no puede superar los 1024 caracteres")
    @Column(name = "url_imagen", nullable = false)
    private String urlImagen;

    public Categoria(Integer id, String titulo, String descripcion, String urlImagen) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.urlImagen = urlImagen;
    }

    public Categoria(String titulo, String descripcion, String urlImagen) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.urlImagen = urlImagen;
    }
}