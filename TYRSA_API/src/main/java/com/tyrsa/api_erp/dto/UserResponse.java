package com.tyrsa.api_erp.dto;

import lombok.Data;

@Data
public class UserResponse {
    private String username;
    private String name;
    private String email;
    private String role;
    private boolean active;
}
