package com.tyrsa.api_erp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String name;
    private String password;
    private String email;
    private String role;  // ejemplo: ROLE_USER, ROLE_ADMIN
    private boolean active;

    // getters y setters
}
