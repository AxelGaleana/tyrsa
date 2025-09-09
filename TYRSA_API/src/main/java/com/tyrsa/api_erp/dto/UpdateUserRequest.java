package com.tyrsa.api_erp.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String name;
    private String email;
    private String role;
    private boolean active;
}