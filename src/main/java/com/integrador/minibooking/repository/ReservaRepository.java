package com.integrador.minibooking.repository;

import com.integrador.minibooking.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    @Query("""
            SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
            FROM Reserva r
            WHERE r.producto.id = :productoId
              AND r.fechaInicio < :fechaFin
              AND r.fechaFin > :fechaInicio
            """)
    boolean existeReservaSolapada(
            @Param("productoId") Integer productoId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    @Query("""
            SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
            FROM Reserva r
            WHERE r.producto.id = :productoId
              AND r.id <> :reservaId
              AND r.fechaInicio < :fechaFin
              AND r.fechaFin > :fechaInicio
            """)
    boolean existeReservaSolapadaExcluyendoReserva(
            @Param("productoId") Integer productoId,
            @Param("reservaId") Integer reservaId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );
}
