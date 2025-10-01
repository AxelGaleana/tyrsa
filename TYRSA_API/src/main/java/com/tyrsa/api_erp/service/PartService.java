package com.tyrsa.api_erp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyrsa.api_erp.model.Part;
import com.tyrsa.api_erp.repository.PartRepository;

@Service
public class PartService {

    @Autowired
    private PartRepository partRepository;

    // Método para registrar usuario
    public Part createPart(Part newPart) {
        if (newPart.getNumeroParte() == null || newPart.getNumeroParte().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de parte es obligatorio.");
        }
        if(partRepository.existsByNumeroParte(newPart.getNumeroParte())) {
            throw new RuntimeException("Numero de parte ya existe");
        }

        return partRepository.save(newPart);
    }


    public Part updatePartByNumeroParte(String numeroParte, Part updatedPart) {
        Part existente = partRepository.findByNumeroParte(numeroParte)
            .orElseThrow(() -> new IllegalArgumentException("La parte con número " + numeroParte + " no existe."));

        existente.setProyecto(updatedPart.getProyecto());
        existente.setDescripcion(updatedPart.getDescripcion());
        existente.setNivelIngenieria(updatedPart.getNivelIngenieria());
        existente.setFechaInicioProyecto(updatedPart.getFechaInicioProyecto());
        existente.setFechaFinProyecto(updatedPart.getFechaFinProyecto());
        existente.setVolumenVendidoProyectoAnual(updatedPart.getVolumenVendidoProyectoAnual());
        existente.setEspecificacionMaterial(updatedPart.getEspecificacionMaterial());
        existente.setTipoProveedor(updatedPart.getTipoProveedor());
        existente.setNombreProveedor(updatedPart.getNombreProveedor());
        existente.setCodigoIdentificacionMaterial(updatedPart.getCodigoIdentificacionMaterial());
        existente.setPresentacionMateriaPrima(updatedPart.getPresentacionMateriaPrima());
        existente.setPesoEstandarPackMP(updatedPart.getPesoEstandarPackMP());
        existente.setDiametroInterno(updatedPart.getDiametroInterno());
        
        existente.setLargoCintaBlank(updatedPart.getLargoCintaBlank());
        existente.setLargoMaterialMaximaTolerancia(updatedPart.getLargoMaterialMaximaTolerancia());
        existente.setAnchoCintaBlank(updatedPart.getAnchoCintaBlank());
        existente.setAnchoMaterialMaximaTolerancia(updatedPart.getAnchoMaterialMaximaTolerancia());
        existente.setEspesor(updatedPart.getEspesor());
        existente.setEspesorMaterialMaximaTolerancia(updatedPart.getEspesorMaterialMaximaTolerancia());

        existente.setCoeficienteMaterial(updatedPart.getCoeficienteMaterial());
        existente.setPesoBlank(updatedPart.getPesoBlank());
        existente.setPesoBlankMax(updatedPart.getPesoBlankMax());
        existente.setPesoPiezaTroquelado(updatedPart.getPesoPiezaTroquelado());
        existente.setPesoPiezaComponente(updatedPart.getPesoPiezaComponente());
        existente.setFactorConsumo(updatedPart.getFactorConsumo());
        existente.setFactorAprovechamiento(updatedPart.getFactorAprovechamiento());
        existente.setMerma(updatedPart.getMerma());

        existente.setCodigoEmpaque(updatedPart.getCodigoEmpaque());
        existente.setFactorConsumoEmpaquePieza(updatedPart.getFactorConsumoEmpaquePieza());
        existente.setPiezasPallet(updatedPart.getPiezasPallet());

        existente.setNumeroOperaciones(updatedPart.getNumeroOperaciones());
        existente.setNumeroMaquinas(updatedPart.getNumeroMaquinas());
        existente.setNumeroOperadores(updatedPart.getNumeroOperadores());
        existente.setNumeroAyudantes(updatedPart.getNumeroAyudantes());
        existente.setPersonalRequerido(updatedPart.getPersonalRequerido());
        existente.setTiempoCicloTotal(updatedPart.getTiempoCicloTotal());
        existente.setTiempoCicloMaximo(updatedPart.getTiempoCicloMaximo());
        existente.setTiempoLlenadoCelula(updatedPart.getTiempoLlenadoCelula());
        existente.setPiezasPorHora(updatedPart.getPiezasPorHora());
        existente.setTiempoTotalCambioModelo(updatedPart.getTiempoTotalCambioModelo());
        existente.setTiempoLiberacion(updatedPart.getTiempoLiberacion());
        existente.setTiempoAjustePorFechador(updatedPart.getTiempoAjustePorFechador());
        existente.setPiezasDeAjuste(updatedPart.getPiezasDeAjuste());
        existente.setCantidadEconomicaPedido(updatedPart.getCantidadEconomicaPedido());

        return partRepository.save(existente);
    }

    public Part getPartByNumeroParte(String numeroParte) {
        return partRepository.findByNumeroParte(numeroParte)
                .orElseThrow(() -> new IllegalArgumentException("La parte con número " + numeroParte + " no existe."));
    }

    public List<Part> getAllParts() {
        return partRepository.findAll();
    }

    
}
