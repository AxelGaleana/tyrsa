package com.tyrsa.api_erp.config;

import com.tyrsa.api_erp.security.JwtFilter;
//import com.tyrsa.api_erp.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    //private final UserService userService;     // Tu UserDetailsService
    private final JwtFilter jwtFilter;         // Tu filtro de JWT

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Usar un Bean externo para CORS es más fiable en Tomcat
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // 2. PERMITIR explícitamente las peticiones OPTIONS (Preflight)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/user/*/reset-password").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Mover la configuración a un Bean independiente
    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        var corsConfig = new org.springframework.web.cors.CorsConfiguration();
        // Agrega tanto localhost como la IP por si acaso
        corsConfig.setAllowedOrigins(List.of("http://localhost:8081", "http://127.0.0.1:8081", "http://localhost:8080", "http://127.0.0.1:8080", "http://server.svrtyrsa.lan:8080", "http://192.168.1.144:8080"));
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("*")); // Permite cualquier cabecera
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L); // Cache de la respuesta preflight

        var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
    
}
