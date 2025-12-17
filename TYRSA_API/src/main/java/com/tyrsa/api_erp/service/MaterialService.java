package com.tyrsa.api_erp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyrsa.api_erp.model.Material;
import com.tyrsa.api_erp.repository.MaterialRepository;

@Service
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;
    
    /**
     * Crear un nuevo material
     */
    public Material crearMaterial(Material material) {
        material.setId(null);
        return materialRepository.save(material);
    }

    /**
     * Actualizar un material existente
     */
    public Material actualizarMaterial(String id, Material materialActualizado) {
        Optional<Material> materialExistenteOpt = materialRepository.findById(id);

        if (materialExistenteOpt.isPresent()) {
            Material materialExistente = materialExistenteOpt.get();
            // Actualizar campos
            materialExistente.setName(materialActualizado.getName());
            materialExistente.setCoeficiente(materialActualizado.getCoeficiente());
            materialExistente.setActivo(materialActualizado.isActivo());

            return materialRepository.save(materialExistente);
        } else {
            throw new RuntimeException("Material con id " + id + " no encontrado");
        }
    }

    /**
     * Obtener todos los materials
     */
    public List<Material> getAll() {
        return materialRepository.findAll();
    }

    /**
     * Obtener los materials activos
     */
    public List<Material> getAllActivos() {
        return materialRepository.findByActivoTrue();
    }

    /**
     * Obtener material por medio de un Id de material
     */
    public Material findById(String id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material no encontrado: " + id));
    }
    
}
