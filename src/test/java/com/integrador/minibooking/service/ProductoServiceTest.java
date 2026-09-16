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
}
