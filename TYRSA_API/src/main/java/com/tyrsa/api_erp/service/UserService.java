package com.tyrsa.api_erp.service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.tyrsa.api_erp.dto.UpdatePasswordRequest;
import com.tyrsa.api_erp.dto.UpdateUserRequest;
import com.tyrsa.api_erp.dto.UserResponse;
import com.tyrsa.api_erp.model.Role;
import com.tyrsa.api_erp.model.User;
import com.tyrsa.api_erp.repository.RoleRepository;
import com.tyrsa.api_erp.repository.UserRepository;
import com.tyrsa.api_erp.security.CustomUserDetails;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Método para registrar usuario
    public User registerUser(String username, String name, String password, String email, String role) {
        if(userRepository.existsByUsername(username)) {
            throw new RuntimeException("Usuario ya existe");
        }
        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password == null ? "Temporal$" + LocalDate.now().getYear() : password));
        user.setEmail(email);
        user.setRole(role);
        user.setActive(true);

        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(user);
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(user -> {
            UserResponse dto = new UserResponse();
            dto.setUsername(user.getUsername());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole());
            dto.setActive(user.isActive());
            return dto;
        }).toList();
    }

    public List<UserResponse> getUsersByRole(String role) {
        List<User> users = userRepository.findByRole(role);
        return users.stream().map(user -> {
            UserResponse dto = new UserResponse();
            dto.setUsername(user.getUsername());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole());
            dto.setActive(user.isActive());
            return dto;
        }).toList();
    }

    public List<UserResponse> getActiveUsers() {
        List<User> users = userRepository.findByActiveTrue();
        return users.stream().map(user -> {
            UserResponse dto = new UserResponse();
            dto.setUsername(user.getUsername());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole());
            dto.setActive(user.isActive());
            return dto;
        }).toList();
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public User updateUserByUsername(String username, UpdateUserRequest request) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setActive(request.isActive());

        return userRepository.save(user);
    }

    public void updateUserPassword(String username, UpdatePasswordRequest request) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar que la nueva contraseña no esté vacía
        if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
            throw new RuntimeException("La nueva contraseña no puede estar vacía");
        }

        // Validar contraseña actual (opcional)
        if (request.getCurrentPassword() != null) {
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new RuntimeException("La contraseña actual no es correcta");
            }
        }

        // Codificar y guardar la nueva contraseña
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void resetPasswordAndSendEmail(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 1. Generar contraseña temporal
        String tempPassword = generateTemporaryPassword();

        // 2. Cifrar la contraseña
        String encodedPassword = passwordEncoder.encode(tempPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);

        // 3. Enviar email
        emailService.sendTemporaryPassword(user.getEmail(), tempPassword);
    }

    private String generateTemporaryPassword() {
        int length = 10;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$!";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }
}
