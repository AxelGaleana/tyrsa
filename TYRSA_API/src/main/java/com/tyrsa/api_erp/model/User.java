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
    private String password;
    private String role;  // ejemplo: ROLE_USER, ROLE_ADMIN

    // getters y setters
}
