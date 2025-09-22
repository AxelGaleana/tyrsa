package com.tyrsa.api_erp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "parts")
public class Part {
    @Id
    private String id;
    private String numeroParte;
    private String proyecto;
    private String descripcion;    
}
