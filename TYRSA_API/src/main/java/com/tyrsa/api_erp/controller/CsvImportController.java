package com.tyrsa.api_erp.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tyrsa.api_erp.service.ImporterService;

@RestController
@RequestMapping("/import")
public class CsvImportController {

    private final ImporterService importer;

    public CsvImportController(ImporterService importer) {
        this.importer = importer;
    }

    @PostMapping(
        value = "/csv",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> importarCsv(
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("El archivo CSV está vacío");
        }

        importer.importarDesdeCsv(file);
        return ResponseEntity.ok("Importación OK");
    }
}
