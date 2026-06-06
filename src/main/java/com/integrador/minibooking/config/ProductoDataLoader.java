package com.integrador.minibooking.config;

import com.integrador.minibooking.model.*;
import com.integrador.minibooking.repository.CategoriaRepository;
import com.integrador.minibooking.repository.CiudadRepository;
import com.integrador.minibooking.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class ProductoDataLoader {

    @Bean
    CommandLineRunner cargarDatos(
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository,
            CiudadRepository ciudadRepository
    ) {
        return args -> {

            if (productoRepository.count() > 0) {
                return;
            }

            Categoria departamento = categoriaRepository.save(
                    new Categoria(
                            "Departamento",
                            "Alojamientos modernos ubicados en zonas céntricas.",
                            "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=800"
                    )
            );

            Categoria casa = categoriaRepository.save(
                    new Categoria(
                            "Casa",
                            "Casas espaciosas ideales para familias o grupos.",
                            "https://images.unsplash.com/photo-1568605114967-8130f3a36994?w=800"
                    )
            );

            Ciudad buenosAires = ciudadRepository.save(
                    new Ciudad(
                            "Buenos Aires",
                            "Argentina",
                            "Buenos Aires"
                    )
            );

            Ciudad bariloche = ciudadRepository.save(
                    new Ciudad(
                            "Bariloche",
                            "Argentina",
                            "Río Negro"
                    )
            );

            Producto producto1 = new Producto(
                    "Departamento céntrico",
                    "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=800",
                    "4.8",
                    new HashSet<>(Set.of(
                            new Descripcion("Departamento ideal para 2 personas, con WiFi, cocina equipada y excelente ubicación.")
                    )),
                    "Departamento moderno en zona céntrica.",
                    true,
                    new HashSet<>(Set.of(
                            new Politica("Normas", "Check-in y check-out", "El ingreso es a partir de las 14 hs y la salida hasta las 10 hs."),
                            new Politica("Seguridad", "Cuidado del alojamiento", "Se solicita respetar las normas del lugar y mantener el espacio en buen estado.")
                    )),
                    departamento,
                    new Ubicacion(
                            "Zona céntrica de Buenos Aires",
                            -34.6037,
                            -58.3816,
                            "Av. Corrientes 1234"
                    ),
                    new HashSet<>(Set.of(
                            new Caracteristica("WiFi", "https://cdn-icons-png.flaticon.com/512/93/93158.png"),
                            new Caracteristica("Cocina", "https://cdn-icons-png.flaticon.com/512/1046/1046857.png")
                    )),
                    new HashSet<>(Set.of(
                            new Imagen("Living", "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=800"),
                            new Imagen("Habitación", "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?w=800")
                    )),
                    buenosAires
            );

            Producto producto2 = new Producto(
                    "Casa con patio",
                    "https://images.unsplash.com/photo-1568605114967-8130f3a36994?w=800",
                    "4.6",
                    new HashSet<>(Set.of(
                            new Descripcion("Casa amplia con patio, ideal para familias y estadías largas.")
                    )),
                    "Casa cómoda en zona tranquila.",
                    true,
                    new HashSet<>(Set.of(
                            new Politica("Normas", "Mascotas", "Se aceptan mascotas pequeñas bajo responsabilidad del huésped."),
                            new Politica("Cancelación", "Política de cancelación", "Cancelación gratuita hasta 48 horas antes del ingreso.")
                    )),
                    casa,
                    new Ubicacion(
                            "Barrio residencial de Bariloche",
                            -41.1335,
                            -71.3103,
                            "Calle Los Lagos 456"
                    ),
                    new HashSet<>(Set.of(
                            new Caracteristica("Patio", "https://cdn-icons-png.flaticon.com/512/616/616408.png"),
                            new Caracteristica("Estacionamiento", "https://cdn-icons-png.flaticon.com/512/2554/2554978.png")
                    )),
                    new HashSet<>(Set.of(
                            new Imagen("Frente", "https://images.unsplash.com/photo-1570129477492-45c003edd2be?w=800"),
                            new Imagen("Patio", "https://images.unsplash.com/photo-1598228723793-52759bba239c?w=800")
                    )),
                    bariloche
            );

            productoRepository.save(producto1);
            productoRepository.save(producto2);
        };
    }
}