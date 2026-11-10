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

            Categoria villa = categoriaRepository.save(
                    new Categoria(
                            "Villa",
                            "Villa turistica",
                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFC7sKO5TFwQ48nNyZ-JaH2rjDzqxHEI9cNM9DWrel2g&s=10"
                    )
            );

            Categoria cabana = categoriaRepository.save(
                    new Categoria(
                            "Cabaña",
                            "Cabañas cálidas en entornos naturales, ideales para descansar entre bosques y montañas.",
                            "https://images.unsplash.com/photo-1739463146961-078f4ac580e3?auto=format&fit=crop&w=1200&q=80"
                    )
            );

            Categoria loft = categoriaRepository.save(
                    new Categoria(
                            "Loft",
                            "Espacios urbanos abiertos, modernos y equipados para estadías cómodas.",
                            "https://images.unsplash.com/photo-1600566753190-17f0baa2a6c3?auto=format&fit=crop&w=1200&q=80"
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

            Ciudad generalRoca = ciudadRepository.save(
                    new Ciudad(
                            "General Roca",
                            "Argentina",
                            "Rio Negro"
                    )
            );

            Ciudad sanMartinDeLosAndes = ciudadRepository.save(
                    new Ciudad(
                            "San Martín de los Andes",
                            "Argentina",
                            "Neuquén"
                    )
            );

            Ciudad mendoza = ciudadRepository.save(
                    new Ciudad(
                            "Mendoza",
                            "Argentina",
                            "Mendoza"
                    )
            );

            Ciudad marDelPlata = ciudadRepository.save(
                    new Ciudad(
                            "Mar del Plata",
                            "Argentina",
                            "Buenos Aires"
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

            Producto producto3 = new Producto(
                    "Villa Turistica",
                    "https://a0.muscache.com/im/pictures/miso/Hosting-1376527271316756582/original/5913c7f9-216d-43f0-a1be-0f38f51ca2f0.jpeg?im_w=720",
                    "5.0",
                    new HashSet<>(Set.of(
                            new Descripcion("Nucleeaaar")
                    )),
                    "Bomba",
                    true,
                    new HashSet<>(Set.of(
                            new Politica("Animales", "Perros", "Se aceptan perros y animales domesticos")
                    )),
                    villa,
                    new Ubicacion(
                            "good",
                            0.0,
                            0.0,
                            "general roca"
                    ),
                    new HashSet<>(Set.of(
                            new Caracteristica(
                                    "Hermosa",
                                    "https://a0.muscache.com/im/pictures/5a2808db-460e-4abe-b5d2-604ea1ac0926.jpg?im_w=720"
                            )
                    )),
                    new HashSet<>(),
                    generalRoca
            );

            Producto producto4 = new Producto(
                    "Cabaña Bosque Andino",
                    "https://images.unsplash.com/photo-1739463146961-078f4ac580e3?auto=format&fit=crop&w=1200&q=80",
                    "4.8",
                    new HashSet<>(Set.of(
                            new Descripcion("Una cabaña patagónica luminosa, construida en madera y pensada para disfrutar la tranquilidad del bosque andino."),
                            new Descripcion("Cuenta con cocina equipada, living con hogar, deck exterior y amplios ventanales con vista a las montañas.")
                    )),
                    "Cabaña de madera rodeada de bosque, con vistas a la cordillera y un ambiente cálido para descansar.",
                    true,
                    new HashSet<>(Set.of(
                            new Politica("Horarios", "Check-in", "Ingreso disponible desde las 15:00."),
                            new Politica("Horarios", "Check-out", "Salida hasta las 11:00."),
                            new Politica("Normas", "No fumar", "No está permitido fumar dentro de la cabaña."),
                            new Politica("Mascotas", "Mascotas bienvenidas", "Se acepta una mascota pequeña con aviso previo.")
                    )),
                    cabana,
                    new Ubicacion(
                            "Entorno boscoso a pocos minutos del centro y del lago Lácar.",
                            -40.1572,
                            -71.3534,
                            "Ruta Provincial 62 km 4"
                    ),
                    new HashSet<>(Set.of(
                            new Caracteristica("WiFi", "https://cdn-icons-png.flaticon.com/512/93/93158.png"),
                            new Caracteristica("Estacionamiento", "https://cdn-icons-png.flaticon.com/512/2554/2554978.png"),
                            new Caracteristica("Cocina equipada", "https://cdn-icons-png.flaticon.com/512/1046/1046857.png"),
                            new Caracteristica("Calefacción", "https://cdn-icons-png.flaticon.com/512/1684/1684375.png"),
                            new Caracteristica("Vista a la montaña", "https://cdn-icons-png.flaticon.com/512/1024/1024651.png")
                    )),
                    new HashSet<>(Set.of(
                            new Imagen("Exterior entre el bosque", "https://images.unsplash.com/photo-1449158743715-0a90ebb6d2d8?auto=format&fit=crop&w=1200&q=80"),
                            new Imagen("Vista de la cabaña", "https://images.unsplash.com/photo-1520984032042-162d526883e0?auto=format&fit=crop&w=1200&q=80"),
                            new Imagen("Entorno natural", "https://images.unsplash.com/photo-1475483768296-6163e08872a1?auto=format&fit=crop&w=1200&q=80"),
                            new Imagen("Interior cálido", "https://images.unsplash.com/photo-1505691938895-1758d7feb511?auto=format&fit=crop&w=1200&q=80")
                    )),
                    sanMartinDeLosAndes
            );

            Producto producto5 = new Producto(
                    "Loft Urbano Mendoza",
                    "https://images.unsplash.com/photo-1600566753190-17f0baa2a6c3?auto=format&fit=crop&w=1200&q=80",
                    "4.7",
                    new HashSet<>(Set.of(
                            new Descripcion("Loft de diseño contemporáneo con espacios integrados, techos altos y grandes ventanales."),
                            new Descripcion("Ideal para parejas o viajeros de trabajo, con cocina completa, escritorio y equipamiento moderno.")
                    )),
                    "Loft moderno y luminoso en el corazón de Mendoza, cerca de restaurantes, plazas y circuitos de bodegas.",
                    true,
                    new HashSet<>(Set.of(
                            new Politica("Horarios", "Check-in", "Ingreso disponible desde las 15:00."),
                            new Politica("Horarios", "Check-out", "Salida hasta las 11:00."),
                            new Politica("Normas", "No fumar", "No está permitido fumar en ningún ambiente."),
                            new Politica("Convivencia", "Sin fiestas", "No se permiten fiestas ni eventos.")
                    )),
                    loft,
                    new Ubicacion(
                            "Zona gastronómica céntrica, con acceso rápido al Parque General San Martín.",
                            -32.8895,
                            -68.8458,
                            "Av. Arístides Villanueva 450"
                    ),
                    new HashSet<>(Set.of(
                            new Caracteristica("WiFi", "https://cdn-icons-png.flaticon.com/512/93/93158.png"),
                            new Caracteristica("Aire acondicionado", "https://cdn-icons-png.flaticon.com/512/911/911409.png"),
                            new Caracteristica("Cocina equipada", "https://cdn-icons-png.flaticon.com/512/1046/1046857.png"),
                            new Caracteristica("Smart TV", "https://cdn-icons-png.flaticon.com/512/2503/2503644.png"),
                            new Caracteristica("Estacionamiento", "https://cdn-icons-png.flaticon.com/512/2554/2554978.png")
                    )),
                    new HashSet<>(Set.of(
                            new Imagen("Living integrado", "https://images.unsplash.com/photo-1600210492486-724fe5c67fb0?auto=format&fit=crop&w=1200&q=80"),
                            new Imagen("Cocina moderna", "https://images.unsplash.com/photo-1600607687920-4e2a09cf159d?auto=format&fit=crop&w=1200&q=80"),
                            new Imagen("Dormitorio", "https://images.unsplash.com/photo-1616486338812-3dadae4b4ace?auto=format&fit=crop&w=1200&q=80"),
                            new Imagen("Detalles de diseño", "https://images.unsplash.com/photo-1600566753086-00f18fb6b3ea?auto=format&fit=crop&w=1200&q=80")
                    )),
                    mendoza
            );

            Producto producto6 = new Producto(
                    "Casa del Mar",
                    "https://images.unsplash.com/photo-1641758260871-97d95519246b?auto=format&fit=crop&w=1200&q=80",
                    "4.9",
                    new HashSet<>(Set.of(
                            new Descripcion("Casa costera de ambientes amplios con terraza y vistas abiertas al Atlántico."),
                            new Descripcion("Dispone de cocina completa, parrilla, piscina y cómodas áreas de descanso para toda la familia.")
                    )),
                    "Casa luminosa frente al mar, con terraza, parrilla y espacios amplios para disfrutar la costa en familia.",
                    true,
                    new HashSet<>(Set.of(
                            new Politica("Horarios", "Check-in", "Ingreso disponible desde las 15:00."),
                            new Politica("Horarios", "Check-out", "Salida hasta las 11:00."),
                            new Politica("Normas", "No fumar", "No está permitido fumar dentro de la casa."),
                            new Politica("Convivencia", "Sin fiestas", "No se permiten fiestas ni eventos sin autorización."),
                            new Politica("Mascotas", "Consultar mascotas", "Las mascotas se aceptan únicamente con consulta previa.")
                    )),
                    casa,
                    new Ubicacion(
                            "Frente a la costa, a pasos de la playa y cerca del centro de Mar del Plata.",
                            -38.0055,
                            -57.5426,
                            "Boulevard Marítimo Patricio Peralta Ramos 3200"
                    ),
                    new HashSet<>(Set.of(
                            new Caracteristica("WiFi", "https://cdn-icons-png.flaticon.com/512/93/93158.png"),
                            new Caracteristica("Vista al mar", "https://cdn-icons-png.flaticon.com/512/2664/2664593.png"),
                            new Caracteristica("Piscina", "https://cdn-icons-png.flaticon.com/512/2784/2784593.png"),
                            new Caracteristica("Parrilla", "https://cdn-icons-png.flaticon.com/512/1142/1142755.png"),
                            new Caracteristica("Estacionamiento", "https://cdn-icons-png.flaticon.com/512/2554/2554978.png"),
                            new Caracteristica("Smart TV", "https://cdn-icons-png.flaticon.com/512/2503/2503644.png")
                    )),
                    new HashSet<>(Set.of(
                            new Imagen("Terraza y piscina", "https://images.unsplash.com/photo-1499793983690-e29da59ef1c2?auto=format&fit=crop&w=1200&q=80"),
                            new Imagen("Playa cercana", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=1200&q=80"),
                            new Imagen("Living con vista", "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?auto=format&fit=crop&w=1200&q=80"),
                            new Imagen("Exterior de la casa", "https://images.unsplash.com/photo-1600047509807-ba8f99d2cdde?auto=format&fit=crop&w=1200&q=80")
                    )),
                    marDelPlata
            );

            productoRepository.save(producto1);
            productoRepository.save(producto2);
            productoRepository.save(producto3);
            productoRepository.save(producto4);
            productoRepository.save(producto5);
            productoRepository.save(producto6);
        };
    }
}
