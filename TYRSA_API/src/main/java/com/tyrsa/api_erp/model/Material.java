package com.tyrsa.api_erp.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "materiales")
public class Material {
    private String id;
    private String name;
    private String coeficiente;
    private boolean activo;
}
