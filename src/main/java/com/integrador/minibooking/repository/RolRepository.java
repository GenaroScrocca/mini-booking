package com.integrador.minibooking.repository;

import com.integrador.minibooking.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

    boolean existsByNombre(String nombre);

    Optional<Rol> findByNombre(String nombre);
}
