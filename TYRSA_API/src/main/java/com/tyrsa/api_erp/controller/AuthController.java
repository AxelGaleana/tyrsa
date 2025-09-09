package com.tyrsa.api_erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.tyrsa.api_erp.dto.AuthRequest;
import com.tyrsa.api_erp.dto.LoginResponse;
import com.tyrsa.api_erp.model.User;
import com.tyrsa.api_erp.security.JwtUtil;
import com.tyrsa.api_erp.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            // Intenta autenticar usuario con Spring Security
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            // Si sale bien, genera token
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Obtener usuario completo desde base de datos
            User user = userService.findByUsername(userDetails.getUsername());

            String token = jwtUtil.generateToken(userDetails);

            LoginResponse response = new LoginResponse(user.getUsername(), user.getName(), user.getRole(), user.getEmail(), token);

            // Retorna token al cliente
            System.out.println("Login exitoso, retornando token");
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos");
        }
    }

    // Logout (solo ejemplo, JWT es stateless)
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // En JWT stateless no se guarda estado en backend
        // Solo se puede borrar token en cliente o implementar blacklist
        return ResponseEntity.ok("Logout exitoso (el cliente debe borrar el token)");
    }
}
