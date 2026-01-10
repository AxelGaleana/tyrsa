package com.tyrsa.api_erp.service;

import org.springframework.stereotype.Service;

import com.tyrsa.api_erp.model.Cliente;
import com.tyrsa.api_erp.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService  {
    @Autowired
    private ClienteRepository clienteRepository;
    
    /**
     * Crear un nuevo cliente
     */
    public Cliente crearCliente(Cliente cliente) {
        cliente.setId(null);
        return clienteRepository.save(cliente);
    }

    /**
     * Actualizar un cliente existente
     */
    public Cliente actualizarCliente(String id, Cliente clienteActualizado) {
        Optional<Cliente> clienteExistenteOpt = clienteRepository.findById(id);

        if (clienteExistenteOpt.isPresent()) {
            Cliente clienteExistente = clienteExistenteOpt.get();
            // Actualizar campos
            clienteExistente.setName(clienteActualizado.getName());
            clienteExistente.setActivo(clienteActualizado.isActivo());

            return clienteRepository.save(clienteExistente);
        } else {
            throw new RuntimeException("Cliente con id " + id + " no encontrado");
        }
    }

    /**
     * Obtener todos los clientes
     */
    public List<Cliente> getAll() {
        return clienteRepository.findAll();
    }

    public Map<String, String> getClientesMap() {
        return clienteRepository.findAll().stream()
                .collect(Collectors.toMap(
                    Cliente::getId,
                    Cliente::getName
                ));
    }


    /**
     * Obtener los clientes activos
     */
    public List<Cliente> getAllActivos() {
        return clienteRepository.findByActivoTrue();
    }

    /**
     * Obtener cliente por medio de un Id de cliente
     */
    public Cliente findById(String id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));
    }

    /**
     * Verifica si existe un cliente por nombre
     */
    public boolean clienteExistePorNombre(String nombre) {
        return clienteRepository.existsByName(nombre);
    }

    /**
     * Obtiene un cliente por nombre
     */
    public Optional<Cliente> obtenerClientePorNombre(String nombre) {
        return clienteRepository.findByName(nombre);
    }

}
