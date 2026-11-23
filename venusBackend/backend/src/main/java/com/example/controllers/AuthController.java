package com.example.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.dtos.*;
import com.example.services.UsuarioService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarios;

    public AuthController(UsuarioService usuarios) {
        this.usuarios = usuarios;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioResponseDTO> cadastro(
            @RequestBody UsuarioRequestDTO d) {
        return ResponseEntity.ok(usuarios.salvar(d));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO d) {
        return ResponseEntity.ok(usuarios.login(d));
    }
}