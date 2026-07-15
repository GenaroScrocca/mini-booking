package com.integrador.minibooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio no puede ser anterior a la fecha actual")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @FutureOrPresent(message = "La fecha de fin no puede ser anterior a la fecha actual")
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @NotNull(message = "El producto es obligatorio")
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Reserva(Integer id, LocalDate fechaInicio, LocalDate fechaFin, Producto producto, Usuario usuario) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.producto = producto;
        this.usuario = usuario;
    }

    public Reserva(LocalDate fechaInicio, LocalDate fechaFin, Producto producto, Usuario usuario) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.producto = producto;
        this.usuario = usuario;
    }
}