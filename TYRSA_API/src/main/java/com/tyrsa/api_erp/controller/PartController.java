package com.tyrsa.api_erp.controller;

import com.tyrsa.api_erp.model.Part;
import com.tyrsa.api_erp.model.PartLog;
import com.tyrsa.api_erp.service.PartService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;


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
            partService.createPart(partRequest, imageFile);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePartById(
            @PathVariable String id,
            @RequestPart("part") Part partRequest,
            @RequestPart(value = "image", required = false) MultipartFile imageFile,
            Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            partService.updatePartById(id, partRequest, imageFile, userDetails);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPartById(@PathVariable String id) {
        try {
            Part parte = partService.getPartById(id);
            return new ResponseEntity<>(parte, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/log/{partId}")
    public ResponseEntity<?> getLogByPartId(@PathVariable String partId) {
        try {
            List<PartLog> log = partService.getLogByPartId(partId);
            return new ResponseEntity<>(log, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/log/approve/{logId}")
    public ResponseEntity<?> approvePartUpdate(@PathVariable String logId, Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            partService.approvePartUpdate(logId, userDetails.getUsername());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/log/deny/{logId}")
    public ResponseEntity<?> denyPartUpdate(@PathVariable String logId, Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            partService.denyPartUpdate(logId, userDetails.getUsername());
            return new ResponseEntity<>(HttpStatus.OK);
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
