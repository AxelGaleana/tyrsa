package com.tyrsa.api_erp.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "partLog")
public class PartLog {
    @Id
    private String id;
    private String oldPartId;
    private String newPartId;
    private LocalDateTime fecha;
    private String estatus;
    private String userName;
    private String aprobador;
    private LocalDate fechaAprobacion;
    private List<CampoActualizado> cambios;    
}
