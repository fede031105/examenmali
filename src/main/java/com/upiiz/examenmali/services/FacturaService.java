package com.upiiz.examenmali.services;

import com.upiiz.examenmali.entities.Factura;
import com.upiiz.examenmali.repositories.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {
    @Autowired
    private FacturaRepository facturaRepository;

    public List<Factura> obtenerTodas() {
        return (List<Factura>) facturaRepository.findAll();
    }

    public void guardarFactura(Factura factura) {
        facturaRepository.save(factura);
    }

    public Optional<Factura> obtenerPorId(Long id) {
        return facturaRepository.findById(id);
    }
}