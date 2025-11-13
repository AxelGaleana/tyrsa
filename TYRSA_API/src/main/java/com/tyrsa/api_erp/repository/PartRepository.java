package com.tyrsa.api_erp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tyrsa.api_erp.model.Part;

import java.util.List;
import java.util.Optional;

public interface PartRepository extends MongoRepository<Part, String> {
    boolean existsByNumeroParte(String numeroParte);
    Optional<Part> findByNumeroParte(String numeroParte);
    Optional<Part> findByNumeroParteAndVersion(String numeroParte, String version);
    List<Part> findAllByVersionOrderByFechaActualizacionDesc(String version);
}
