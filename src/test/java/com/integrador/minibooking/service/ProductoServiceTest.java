package com.integrador.minibooking.service;

import com.integrador.minibooking.exception.BadRequestException;
import com.integrador.minibooking.exception.ResourceNotFoundException;
import com.integrador.minibooking.model.Categoria;
import com.integrador.minibooking.model.Ciudad;
import com.integrador.minibooking.model.Producto;
import com.integrador.minibooking.repository.CategoriaRepository;
import com.integrador.minibooking.repository.CiudadRepository;
import com.integrador.minibooking.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private CiudadRepository ciudadRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void guardarConCategoriaSinIdLanzaBadRequestException() {
        Producto producto = new Producto();
        producto.setCategoria(new Categoria());
        Ciudad ciudad = new Ciudad();
        ciudad.setId(1);
        producto.setCiudad(ciudad);

        assertThrows(BadRequestException.class, () -> productoService.guardar(producto));

        verifyNoInteractions(categoriaRepository, ciudadRepository);
        verify(productoRepository, never()).save(producto);
    }

    @Test
    void guardarConCategoriaInexistenteLanzaResourceNotFoundException() {
        Producto producto = new Producto();
        Categoria categoria = new Categoria();
        categoria.setId(99);
        Ciudad ciudad = new Ciudad();
        ciudad.setId(1);
        producto.setCategoria(categoria);
        producto.setCiudad(ciudad);

        when(categoriaRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productoService.guardar(producto)
        );

        assertEquals("No se encontró la categoría con id 99", exception.getMessage());
        verifyNoInteractions(ciudadRepository);
        verify(productoRepository, never()).save(producto);
    }

    @Test
    void buscarDisponiblesDevuelveProductosSinReservasSuperpuestas() {
        LocalDate fechaInicio = LocalDate.of(2026, 11, 20);
        LocalDate fechaFin = LocalDate.of(2026, 11, 25);
        Producto producto1 = crearProducto(1);
        Producto producto2 = crearProducto(2);

        when(productoRepository.findDisponiblesEntreFechas(fechaInicio, fechaFin))
                .thenReturn(List.of(producto1, producto2));

        List<Producto> resultado = productoService.buscarDisponibles(fechaInicio, fechaFin);

        assertEquals(List.of(producto1, producto2), resultado);
    }

    @Test
    void buscarDisponiblesExcluyeProductoOcupado() {
        LocalDate fechaInicio = LocalDate.of(2026, 11, 20);
        LocalDate fechaFin = LocalDate.of(2026, 11, 25);
        Producto productoDisponible = crearProducto(2);

        when(productoRepository.findDisponiblesEntreFechas(fechaInicio, fechaFin))
                .thenReturn(List.of(productoDisponible));

        List<Producto> resultado = productoService.buscarDisponibles(fechaInicio, fechaFin);

        assertEquals(List.of(productoDisponible), resultado);
        verify(productoRepository).findDisponiblesEntreFechas(fechaInicio, fechaFin);
    }

    @Test
    void buscarDisponiblesPermiteComenzarCuandoOtraReservaTermina() {
        LocalDate finReservaExistente = LocalDate.of(2026, 11, 15);
        LocalDate fechaFinBuscada = LocalDate.of(2026, 11, 20);
        Producto producto = crearProducto(1);

        when(productoRepository.findDisponiblesEntreFechas(finReservaExistente, fechaFinBuscada))
                .thenReturn(List.of(producto));

        List<Producto> resultado = productoService.buscarDisponibles(finReservaExistente, fechaFinBuscada);

        assertEquals(List.of(producto), resultado);
    }

    @Test
    void buscarDisponiblesConFechaFinNoPosteriorLanzaBadRequestException() {
        LocalDate fecha = LocalDate.of(2026, 11, 20);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> productoService.buscarDisponibles(fecha, fecha)
        );

        assertEquals("La fecha de fin debe ser posterior a la fecha de inicio", exception.getMessage());
        verifyNoInteractions(productoRepository);
    }

    private Producto crearProducto(Integer id) {
        Producto producto = new Producto();
        producto.setId(id);
        return producto;
    }
}
