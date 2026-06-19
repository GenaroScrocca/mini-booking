package com.integrador.minibooking.service;

import com.integrador.minibooking.model.Categoria;
import com.integrador.minibooking.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Integer id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    public Categoria guardar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Integer id, Categoria categoriaActualizada) {
        Categoria categoriaExistente = categoriaRepository.findById(id).orElse(null);

        if (categoriaExistente == null) {
            return null;
        }

        categoriaExistente.setTitulo(categoriaActualizada.getTitulo());
        categoriaExistente.setDescripcion(categoriaActualizada.getDescripcion());
        categoriaExistente.setUrlImagen(categoriaActualizada.getUrlImagen());

        return categoriaRepository.save(categoriaExistente);
    }

    public void eliminar(Integer id) {
        categoriaRepository.deleteById(id);
    }
}