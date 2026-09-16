package com.integrador.minibooking.repository;

import com.integrador.minibooking.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByCiudadId(Integer ciudadId);

    List<Producto> findByCategoriaId(Integer categoriaId);
}
