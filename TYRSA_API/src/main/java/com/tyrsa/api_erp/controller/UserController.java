package com.tyrsa.api_erp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tyrsa.api_erp.dto.RegisterRequest;
import com.tyrsa.api_erp.dto.UpdatePasswordRequest;
import com.tyrsa.api_erp.dto.UpdateUserRequest;
import com.tyrsa.api_erp.dto.UserResponse;
import com.tyrsa.api_erp.model.Role;
import com.tyrsa.api_erp.model.User;
import com.tyrsa.api_erp.service.UserService;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Endpoint para registro
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            userService.registerUser(request.getUsername(), request.getName(), request.getPassword(), request.getEmail(), request.getRole());
            return ResponseEntity.ok("Usuario registrado con éxito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/getAllRoles")
    public List<Role> getAllRoles() {
        return userService.getAllRoles();
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody UpdateUserRequest request) {
        try {
            User updatedUser = userService.updateUserByUsername(username, request);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Aquí atrapas otras excepciones que puedan ser 403 o similares
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PutMapping("/{username}/password")
    public ResponseEntity<?> updatePassword(
        @PathVariable String username,
        @RequestBody UpdatePasswordRequest request) {

        try {
            userService.updateUserPassword(username, request);
            return ResponseEntity.ok("Contraseña actualizada con éxito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la contraseña");
        }
    }

    @PostMapping("/{username}/reset-password")
    public ResponseEntity<?> resetPassword(@PathVariable String username) {
        try {
            userService.resetPasswordAndSendEmail(username);
            return ResponseEntity.ok("Contraseña temporal enviada al correo del usuario");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al restablecer la contraseña");
        }
    }
}
