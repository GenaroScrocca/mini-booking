package com.integrador.minibooking.config;

import com.integrador.minibooking.model.Rol;
import com.integrador.minibooking.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RolDataLoader {

    @Bean
    CommandLineRunner cargarRoles(RolRepository rolRepository) {
        return args -> {
            crearRolSiNoExiste(rolRepository, "CLIENTE");
            crearRolSiNoExiste(rolRepository, "ADMIN");
        };
    }

    private void crearRolSiNoExiste(RolRepository rolRepository, String nombre) {
        if (!rolRepository.existsByNombre(nombre)) {
            rolRepository.save(new Rol(nombre));
        }
    }
}
