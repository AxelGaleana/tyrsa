package com.tyrsa.api_erp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "parts")
public class Part {
    @Id
    private String id;
    private String rootPartId;
    private String numeroParte;
    private String proyecto;
    private String descripcion;
    private String nivelIngenieria;
    private LocalDate fechaInicioProyecto;
    private LocalDate fechaFinProyecto;
    private String volumenVendidoProyectoAnual;
    private String especificacionMaterial;
    private String tipoProveedor;
    private String nombreProveedor;
    private String codigoIdentificacionMaterial;
    private String presentacionMateriaPrima;
    private String pesoEstandarPackMP;
    private String diametroInterno;
    private String diametroExterno;
    private String largoCintaBlank;
    private String largoMaterialMaximaTolerancia;
    private String anchoCintaBlank;
    private String anchoMaterialMaximaTolerancia;
    private String espesor;
    private String espesorMaterialMaximaTolerancia;
    private String coeficienteMaterial;
    //private String pesoBlank;
    private String pesoBlankMax;
    private String pesoPiezaTroquelado;
    private String pesoPiezaComponente;
    private String factorConsumo;
    //private String factorAprovechamiento;
    //private String merma;
    private String codigoEmpaque;
    private String factorConsumoEmpaquePieza;
    private String piezasPallet;
    private String numeroOperaciones;
    private String numeroMaquinas;
    private String numeroOperadores;
    private String numeroAyudantes;
    private String personalRequerido;
    private String tiempoCicloTotal;
    private String tiempoCicloMaximo;
    private String wipPorMaquina;
    //private String tiempoLlenadoCelula;
    private String piezasPorHora;
    private String tiempoTotalCambioModelo;
    private String tiempoLiberacion;
    private String tiempoAjustePorFechador;
    private String piezasDeAjuste;
    private String cantidadEconomicaPedido;
    private Componente componentes[];
    private RutaFabricacion rutas[];
    private String fileName;
    private String version;
    private LocalDateTime fechaActualizacion;
    private boolean actualizacionPendiente;
}
