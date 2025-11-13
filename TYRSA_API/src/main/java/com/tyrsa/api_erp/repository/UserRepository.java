package com.tyrsa.api_erp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tyrsa.api_erp.dto.UserResponse;
import com.tyrsa.api_erp.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    List<User> findByRole(String role);
}
