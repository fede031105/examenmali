package com.upiiz.examenmali.repositories;

import com.upiiz.examenmali.entities.Producto;
import org.springframework.data.repository.CrudRepository;

public interface ProductoRepository extends CrudRepository<Producto, Long> {
}