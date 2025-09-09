package com.tyrsa.api_erp.repository;

import com.tyrsa.api_erp.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByRole(String role);

    boolean existsByRole(String role);
}