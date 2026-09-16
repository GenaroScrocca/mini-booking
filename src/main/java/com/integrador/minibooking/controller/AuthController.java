package com.integrador.minibooking.controller;

import com.integrador.minibooking.dto.LoginRequestDTO;
import com.integrador.minibooking.dto.RegistroRequestDTO;
import com.integrador.minibooking.dto.UsuarioResponseDTO;
import com.integrador.minibooking.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public UsuarioResponseDTO registrar(@Valid @RequestBody RegistroRequestDTO registroRequestDTO) {
        return authService.registrar(registroRequestDTO);
    }

    @PostMapping("/login")
    public UsuarioResponseDTO login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return authService.login(loginRequestDTO);
    }
}
