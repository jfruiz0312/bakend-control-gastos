package com.fernandoruiz.app.management.controller;

import com.fernandoruiz.app.management.config.JwtUtil;
import com.fernandoruiz.app.management.dto.LoginDto;
import com.fernandoruiz.app.management.dto.response.ApiResponse;
import com.fernandoruiz.app.management.dto.response.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginDto loginDto) {
        log.info("Intento de login para el usuario: {}", loginDto.getUsername());
        UsernamePasswordAuthenticationToken login =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(login);

        if (!authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("No fue posible autenticar las credenciales enviadas");
        }

        String username = authentication.getName();
        String jwt = this.jwtUtil.create(username);
        LoginResponse response = LoginResponse.builder()
                .username(username)
                .tokenType("Bearer")
                .accessToken(jwt)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .body(ApiResponse.success("Login exitoso", response));
    }
}
