package com.upiiz.examenmali.services;

import com.upiiz.examenmali.entities.Cliente;
import com.upiiz.examenmali.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // Obtener la lista completa de clientes (Para el listado)
    public List<Cliente> obtenerTodos() {
        return (List<Cliente>) clienteRepository.findAll();
    }

    // Guardar un nuevo cliente o actualizar uno existente (Para crear/editar)
    public void guardarCliente(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    // Buscar a un cliente por su ID (Para cargar sus datos al editar)
    public Optional<Cliente> obtenerPorId(Long id) {
        return clienteRepository.findById(id);
    }

    // Eliminar a un cliente permanentemente de la base de datos
    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }
}