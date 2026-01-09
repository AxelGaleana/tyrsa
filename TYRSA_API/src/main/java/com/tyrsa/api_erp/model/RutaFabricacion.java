package com.tyrsa.api_erp.model;

import lombok.Data;

@Data
public class RutaFabricacion {
    private String id;
    private String operacion;
    private String numeroMaquina;
    private String tonelaje;
    private String descripcion;
    private String tiempoCiclo;
    private String numeroMaquinas;
    private String numeroOperadores;
    private String numeroAyudantes;
}
