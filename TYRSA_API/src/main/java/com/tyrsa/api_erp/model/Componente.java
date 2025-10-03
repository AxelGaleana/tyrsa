package com.tyrsa.api_erp.model;

import lombok.Data;

@Data
public class Componente {
    private String id;
    private String especificacionComponente;
    private String tipoProveedor;
    private String nombreProveedor;
    private String codigoIdentificacionComponente;
    private String cantidadComponentesPorPieza;    
}
