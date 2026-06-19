package com.integrador.minibooking.service;

import com.integrador.minibooking.model.Ciudad;
import com.integrador.minibooking.repository.CiudadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CiudadService {

    private final CiudadRepository ciudadRepository;

    public CiudadService(CiudadRepository ciudadRepository) {
        this.ciudadRepository = ciudadRepository;
    }

    public List<Ciudad> listarTodas() {
        return ciudadRepository.findAll();
    }

    public Ciudad buscarPorId(Integer id) {
        return ciudadRepository.findById(id).orElse(null);
    }

    public Ciudad guardar(Ciudad ciudad) {
        return ciudadRepository.save(ciudad);
    }

    public Ciudad actualizar(Integer id, Ciudad ciudadActualizada) {
        Ciudad ciudadExistente = ciudadRepository.findById(id).orElse(null);

        if (ciudadExistente == null) {
            return null;
        }

        ciudadExistente.setNombre(ciudadActualizada.getNombre());
        ciudadExistente.setPais(ciudadActualizada.getPais());
        ciudadExistente.setProvincia(ciudadActualizada.getProvincia());

        return ciudadRepository.save(ciudadExistente);
    }

    public void eliminar(Integer id) {
        ciudadRepository.deleteById(id);
    }
}