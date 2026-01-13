package com.tyrsa.api_erp.repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.mongodb.repository.MongoRepository;
import com.tyrsa.api_erp.model.Cliente;

public interface ClienteRepository extends MongoRepository<Cliente, String> {
    List<Cliente> findByActivoTrueOrderByNameAsc();
    boolean existsByName(String name);
    Optional<Cliente> findByName(String name);
    List<Cliente> findAllByOrderByNameAsc();
}
