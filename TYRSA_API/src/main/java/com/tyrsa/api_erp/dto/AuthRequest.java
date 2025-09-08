package com.tyrsa.api_erp.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;

    // getters y setters
}
