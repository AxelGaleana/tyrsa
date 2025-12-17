package com.tyrsa.api_erp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tyrsa.api_erp.model.Material;
import com.tyrsa.api_erp.service.MaterialService;

@RestController
@RequestMapping("/materiales")
public class MaterialController {

    @Autowired
    private MaterialService materialService;
    
    /**
     * Crear un nuevo material
     * POST /api/materials
     */
    @PostMapping
    public ResponseEntity<Material> crearMaterial(@RequestBody Material material) {
        Material creado = materialService.crearMaterial(material);
        return ResponseEntity.ok(creado);
    }

    /**
     * Actualizar un material existente
     * PUT /api/materials/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Material> actualizarMaterial(
            @PathVariable String id,
            @RequestBody Material material) {
        Material actualizado = materialService.actualizarMaterial(id, material);
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterial(@PathVariable String id) {
        Material material = materialService.findById(id);
        return ResponseEntity.ok(material);
    }

    @GetMapping("/getAllMaterials")
    public ResponseEntity<List<Material>> getAllMaterials() {
        List<Material> materials = materialService.getAll();
        return ResponseEntity.ok(materials);
    }

    @GetMapping("/getAllMaterialsActvos")
    public ResponseEntity<List<Material>> getAllMaterialsActvos() {
        List<Material> materials = materialService.getAllActivos();
        return ResponseEntity.ok(materials);
    }
}
