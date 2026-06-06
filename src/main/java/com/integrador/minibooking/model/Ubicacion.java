package com.integrador.minibooking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ubicaciones")
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "latitud", nullable = false)
    private Double latitud;

    @Column(name = "longitud", nullable = false)
    private Double longitud;

    @Column(name = "direccion", nullable = false)
    private String direccion;

    public Ubicacion(Integer id, String descripcion, Double latitud, Double longitud, String direccion) {
        this.id = id;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
    }

    public Ubicacion(String descripcion, Double latitud, Double longitud, String direccion) {
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
    }
}