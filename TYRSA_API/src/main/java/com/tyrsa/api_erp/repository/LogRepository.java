package com.tyrsa.api_erp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tyrsa.api_erp.model.PartLog;

@Repository
public interface LogRepository extends MongoRepository<PartLog, String> {
    List<PartLog> findByRootPartId(String oldPartId);
}