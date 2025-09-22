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
    public Part createPart(String numeroParte, String proyecto, String descripcion) {
        if (numeroParte == null || numeroParte.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de parte es obligatorio.");
        }
        if(partRepository.existsByNumeroParte(numeroParte)) {
            throw new RuntimeException("Numero de parte ya existe");
        }
        Part parte = new Part();
        parte.setNumeroParte(numeroParte);
        parte.setProyecto(proyecto);
        parte.setDescripcion(descripcion);

        return partRepository.save(parte);
    }

    public Part updatePartByNumeroParte(String numeroParte, String nuevoProyecto, String nuevaDescripcion) {
        Part existente = partRepository.findByNumeroParte(numeroParte)
            .orElseThrow(() -> new IllegalArgumentException("La parte con número " + numeroParte + " no existe."));

        existente.setProyecto(nuevoProyecto);
        existente.setDescripcion(nuevaDescripcion);

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
