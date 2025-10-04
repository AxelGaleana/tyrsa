package com.tyrsa.api_erp.controller;

import com.tyrsa.api_erp.model.Part;
import com.tyrsa.api_erp.service.PartService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/parts")
public class PartController {

    @Autowired
    private PartService partService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPart(
            @RequestPart("part") Part partRequest,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {

        try {
            Part createdPart = partService.createPart(partRequest, imageFile);
            return new ResponseEntity<>(createdPart, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{numeroParte}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePartByNumeroParte(
            @PathVariable String numeroParte,
            @RequestPart("part") Part partRequest,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        try {
            Part updatedPart = partService.updatePartByNumeroParte(numeroParte, partRequest, imageFile);
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
