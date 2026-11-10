package com.integrador.minibooking.repository;

import com.integrador.minibooking.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByCiudadId(Integer ciudadId);

    List<Producto> findByCategoriaId(Integer categoriaId);

    @Query("""
            SELECT p
            FROM Producto p
            WHERE NOT EXISTS (
                SELECT r.id
                FROM Reserva r
                WHERE r.producto = p
                  AND r.fechaInicio < :fechaFin
                  AND r.fechaFin > :fechaInicio
            )
            """)
    List<Producto> findDisponiblesEntreFechas(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );
}
