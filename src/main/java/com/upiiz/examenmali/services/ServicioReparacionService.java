package com.upiiz.examenmali.services;

import com.upiiz.examenmali.entities.ServicioReparacion;
import com.upiiz.examenmali.repositories.ServicioReparacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioReparacionService {
    @Autowired
    private ServicioReparacionRepository servicioReparacionRepository;

    public List<ServicioReparacion> obtenerTodos() {
        return (List<ServicioReparacion>) servicioReparacionRepository.findAll();
    }

    public void guardarServicio(ServicioReparacion servicio) {
        servicioReparacionRepository.save(servicio);
    }

    public Optional<ServicioReparacion> obtenerPorId(Long id) {
        return servicioReparacionRepository.findById(id);
    }

    public void eliminarServicio(Long id) {
        servicioReparacionRepository.deleteById(id);
    }
}