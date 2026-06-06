package com.integrador.minibooking.model;

import jakarta.persistence.*;
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

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "imagen_principal_url", nullable = false)
    private String imagenPrincipalUrl;

    @Column(name = "puntaje", nullable = false)
    private String puntaje;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "producto_id")
    private Set<Descripcion> descripciones = new HashSet<>();

    @Column(name = "descripcion_card", nullable = false)
    private String descripcionCard;

    @Column(name = "disponibilidad", nullable = false)
    private Boolean disponible;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "producto_id")
    private Set<Politica> politica = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "producto_id")
    private Set<Caracteristica> caracteristicas = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "producto_id")
    private Set<Imagen> imagenesSecundarias = new HashSet<>();

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