package com.upiiz.examenmali.repositories;

import com.upiiz.examenmali.entities.Cliente;
import org.springframework.data.repository.CrudRepository;

public interface ClienteRepository extends CrudRepository<Cliente, Long> {
}