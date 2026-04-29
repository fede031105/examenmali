package com.upiiz.examenmali.repositories;

import com.upiiz.examenmali.entities.Factura;
import org.springframework.data.repository.CrudRepository;

public interface FacturaRepository extends CrudRepository<Factura, Long> {
}