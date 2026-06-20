package com.integrador.minibooking.service;

import com.integrador.minibooking.exception.ResourceNotFoundException;
import com.integrador.minibooking.model.Producto;
import com.integrador.minibooking.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Producto buscarPorId(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con id " + id));
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizar(Integer id, Producto productoActualizado) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con id " + id));

        productoExistente.setTitulo(productoActualizado.getTitulo());
        productoExistente.setImagenPrincipalUrl(productoActualizado.getImagenPrincipalUrl());
        productoExistente.setPuntaje(productoActualizado.getPuntaje());
        productoExistente.setDescripciones(productoActualizado.getDescripciones());
        productoExistente.setDescripcionCard(productoActualizado.getDescripcionCard());
        productoExistente.setDisponible(productoActualizado.getDisponible());
        productoExistente.setPolitica(productoActualizado.getPolitica());
        productoExistente.setCategoria(productoActualizado.getCategoria());
        productoExistente.setUbicacion(productoActualizado.getUbicacion());
        productoExistente.setCaracteristicas(productoActualizado.getCaracteristicas());
        productoExistente.setImagenesSecundarias(productoActualizado.getImagenesSecundarias());
        productoExistente.setCiudad(productoActualizado.getCiudad());

        return productoRepository.save(productoExistente);
    }

    public void eliminar(Integer id) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con id " + id));

        productoRepository.delete(productoExistente);
    }
}