package com.integrador.minibooking.service;

import com.integrador.minibooking.exception.ResourceNotFoundException;
import com.integrador.minibooking.model.Rol;
import com.integrador.minibooking.repository.RolRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }

    public Rol buscarPorId(Integer id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el rol con id " + id));
    }

    public Rol guardar(Rol rol) {
        return rolRepository.save(rol);
    }

    public Rol actualizar(Integer id, Rol rolActualizado) {
        Rol rolExistente = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el rol con id " + id));

        rolExistente.setNombre(rolActualizado.getNombre());

        return rolRepository.save(rolExistente);
    }

    public void eliminar(Integer id) {
        Rol rolExistente = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el rol con id " + id));

        rolRepository.delete(rolExistente);
    }
}