package com.tyrsa.api_erp.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "clientes")
public class Cliente {
    private String id;
    private String name;
    private boolean activo;
}
