package com.integrador.minibooking.service;

import com.integrador.minibooking.exception.BadRequestException;
import com.integrador.minibooking.exception.ResourceNotFoundException;
import com.integrador.minibooking.model.Categoria;
import com.integrador.minibooking.model.Ciudad;
import com.integrador.minibooking.model.Producto;
import com.integrador.minibooking.repository.CategoriaRepository;
import com.integrador.minibooking.repository.CiudadRepository;
import com.integrador.minibooking.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final CiudadRepository ciudadRepository;

    public ProductoService(
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository,
            CiudadRepository ciudadRepository
    ) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.ciudadRepository = ciudadRepository;
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public List<Producto> buscarPorCiudad(Integer ciudadId) {
        return productoRepository.findByCiudadId(ciudadId);
    }

    public List<Producto> buscarPorCategoria(Integer categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    public Producto buscarPorId(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con id " + id));
    }

    public Producto guardar(Producto producto) {
        Categoria categoria = buscarCategoriaDelProducto(producto);
        Ciudad ciudad = buscarCiudadDelProducto(producto);

        producto.setCategoria(categoria);
        producto.setCiudad(ciudad);

        return productoRepository.save(producto);
    }

    public Producto actualizar(Integer id, Producto productoActualizado) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con id " + id));

        Categoria categoria = buscarCategoriaDelProducto(productoActualizado);
        Ciudad ciudad = buscarCiudadDelProducto(productoActualizado);

        productoExistente.setTitulo(productoActualizado.getTitulo());
        productoExistente.setImagenPrincipalUrl(productoActualizado.getImagenPrincipalUrl());
        productoExistente.setPuntaje(productoActualizado.getPuntaje());
        productoExistente.setDescripciones(productoActualizado.getDescripciones());
        productoExistente.setDescripcionCard(productoActualizado.getDescripcionCard());
        productoExistente.setDisponible(productoActualizado.getDisponible());
        productoExistente.setPolitica(productoActualizado.getPolitica());
        productoExistente.setCategoria(categoria);
        productoExistente.setUbicacion(productoActualizado.getUbicacion());
        productoExistente.setCaracteristicas(productoActualizado.getCaracteristicas());
        productoExistente.setImagenesSecundarias(productoActualizado.getImagenesSecundarias());
        productoExistente.setCiudad(ciudad);

        return productoRepository.save(productoExistente);
    }

    public void eliminar(Integer id) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con id " + id));

        productoRepository.delete(productoExistente);
    }

    private Categoria buscarCategoriaDelProducto(Producto producto) {
        if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
            throw new BadRequestException("El id de la categoría es obligatorio para el producto");
        }

        Integer categoriaId = producto.getCategoria().getId();

        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la categoría con id " + categoriaId));
    }

    private Ciudad buscarCiudadDelProducto(Producto producto) {
        if (producto.getCiudad() == null || producto.getCiudad().getId() == null) {
            throw new BadRequestException("El id de la ciudad es obligatorio para el producto");
        }

        Integer ciudadId = producto.getCiudad().getId();

        return ciudadRepository.findById(ciudadId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la ciudad con id " + ciudadId));
    }
}
