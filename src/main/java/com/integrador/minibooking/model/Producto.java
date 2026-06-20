package com.integrador.minibooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El título del producto es obligatorio")
    @Size(max = 150, message = "El título del producto no puede superar los 150 caracteres")
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @NotBlank(message = "La imagen principal es obligatoria")
    @Size(max = 1024, message = "La URL de la imagen principal no puede superar los 1024 caracteres")
    @Column(name = "imagen_principal_url", nullable = false)
    private String imagenPrincipalUrl;

    @NotBlank(message = "El puntaje es obligatorio")
    @Size(max = 10, message = "El puntaje no puede superar los 10 caracteres")
    @Column(name = "puntaje", nullable = false)
    private String puntaje;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "producto_id")
    private Set<Descripcion> descripciones = new HashSet<>();

    @NotBlank(message = "La descripción para la card es obligatoria")
    @Size(max = 500, message = "La descripción para la card no puede superar los 500 caracteres")
    @Column(name = "descripcion_card", nullable = false)
    private String descripcionCard;

    @NotNull(message = "La disponibilidad es obligatoria")
    @Column(name = "disponibilidad", nullable = false)
    private Boolean disponible;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "producto_id")
    private Set<Politica> politica = new HashSet<>();

    @NotNull(message = "La categoría es obligatoria")
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @NotNull(message = "La ubicación es obligatoria")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "producto_id")
    private Set<Caracteristica> caracteristicas = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "producto_id")
    private Set<Imagen> imagenesSecundarias = new HashSet<>();

    @NotNull(message = "La ciudad es obligatoria")
    @ManyToOne
    @JoinColumn(name = "ciudad_id", nullable = false)
    private Ciudad ciudad;

    public Producto(Integer id, String titulo, String imagenPrincipalUrl, String puntaje,
                    Set<Descripcion> descripciones, String descripcionCard, Boolean disponible,
                    Set<Politica> politica, Categoria categoria, Ubicacion ubicacion,
                    Set<Caracteristica> caracteristicas, Set<Imagen> imagenesSecundarias,
                    Ciudad ciudad) {
        this.id = id;
        this.titulo = titulo;
        this.imagenPrincipalUrl = imagenPrincipalUrl;
        this.puntaje = puntaje;
        this.descripciones = descripciones;
        this.descripcionCard = descripcionCard;
        this.disponible = disponible;
        this.politica = politica;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.caracteristicas = caracteristicas;
        this.imagenesSecundarias = imagenesSecundarias;
        this.ciudad = ciudad;
    }

    public Producto(String titulo, String imagenPrincipalUrl, String puntaje,
                    Set<Descripcion> descripciones, String descripcionCard, Boolean disponible,
                    Set<Politica> politica, Categoria categoria, Ubicacion ubicacion,
                    Set<Caracteristica> caracteristicas, Set<Imagen> imagenesSecundarias,
                    Ciudad ciudad) {
        this.titulo = titulo;
        this.imagenPrincipalUrl = imagenPrincipalUrl;
        this.puntaje = puntaje;
        this.descripciones = descripciones;
        this.descripcionCard = descripcionCard;
        this.disponible = disponible;
        this.politica = politica;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.caracteristicas = caracteristicas;
        this.imagenesSecundarias = imagenesSecundarias;
        this.ciudad = ciudad;
    }
}