package com.tyrsa.api_erp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/protegido")
    public String endpointProtegido() {
        return "✅ Hola, estás autenticado con JWT.";
    }
}
