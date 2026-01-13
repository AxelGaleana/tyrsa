package com.tyrsa.api_erp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.tyrsa.api_erp.model.Material;

public interface MaterialRepository  extends MongoRepository<Material, String> {
    List<Material> findByActivoTrueOrderByNameAsc();
    List<Material> findAllByOrderByNameAsc();
}
