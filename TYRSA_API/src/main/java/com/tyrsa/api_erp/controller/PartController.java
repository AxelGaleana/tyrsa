package com.tyrsa.api_erp.controller;

import com.tyrsa.api_erp.model.Part;
import com.tyrsa.api_erp.service.PartService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parts")
public class PartController {

    @Autowired
    private PartService partService;

    @PostMapping
    public ResponseEntity<?> createPart(@RequestBody Part partRequest) {
        try {
            Part createdPart = partService.createPart(partRequest);
            
            return new ResponseEntity<>(createdPart, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{numeroParte}")
    public ResponseEntity<?> updatePartByNumeroParte(
            @PathVariable String numeroParte,
            @RequestBody Part partRequest) {
        try {
            Part updatedPart = partService.updatePartByNumeroParte(numeroParte, partRequest);
            return new ResponseEntity<>(updatedPart, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{numeroParte}")
    public ResponseEntity<?> getPartByNumeroParte(@PathVariable String numeroParte) {
        try {
            Part parte = partService.getPartByNumeroParte(numeroParte);
            return new ResponseEntity<>(parte, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllParts() {
        try {
            List<Part> partes = partService.getAllParts();
            return new ResponseEntity<>(partes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
