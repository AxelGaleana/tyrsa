package com.tyrsa.api_erp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.tyrsa.api_erp.model.Cliente;

public interface ClienteRepository extends MongoRepository<Cliente, String> {
    List<Cliente> findByActivoTrue();
}
