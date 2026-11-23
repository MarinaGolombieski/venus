package com.example.services;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dtos.*;
import com.example.entities.Usuario;
import com.example.repositories.UsuarioRepository;

import Enum.Role;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    public List<UsuarioResponseDTO> listarTodos() {
        return repo.findAll().stream().map(this::dto).toList();
    }

    public Usuario entity(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public UsuarioResponseDTO buscar(Long id) {
        return dto(entity(id));
    }

    public UsuarioResponseDTO salvar(UsuarioRequestDTO d) {

        if (d == null || d.getUsername() == null || d.getUsername().isBlank()
                || d.getSenha() == null || d.getSenha().isBlank()) {
            throw new RuntimeException("username e senha são obrigatórios");
        }

        if (repo.findByUsername(d.getUsername().trim()).isPresent()) {
            throw new RuntimeException("Usuário já cadastrado");
        }

        Usuario u = new Usuario();

        u.setUsername(d.getUsername().trim());
        u.setNome(d.getNome());
        u.setTelefone(d.getTelefone());
        u.setFotoPerfil(d.getFotoPerfil());
        u.setSenha(encoder.encode(d.getSenha()));
        u.setRole(d.getRole() == null ? Role.CLIENTE : d.getRole());

        // Salvar endereço
        u.setCep(d.getCep());
        u.setRua(d.getRua());
        u.setNumero(d.getNumero());
        u.setComplemento(d.getComplemento());
        u.setBairro(d.getBairro());
        u.setCidade(d.getCidade());
        u.setEstado(d.getEstado());

        return dto(repo.save(u));
    }

    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO d) {

        Usuario u = entity(id);

        if (d.getUsername() != null && !d.getUsername().isBlank()) {
            u.setUsername(d.getUsername().trim());
        }

        if (d.getSenha() != null && !d.getSenha().isBlank()) {
            u.setSenha(encoder.encode(d.getSenha()));
        }

        if (d.getRole() != null) {
            u.setRole(d.getRole());
        }

        if (d.getNome() != null) {
            u.setNome(d.getNome());
        }

        if (d.getTelefone() != null) {
            u.setTelefone(d.getTelefone());
        }

        if (d.getFotoPerfil() != null) {
            u.setFotoPerfil(d.getFotoPerfil());
        }

        // Atualizar endereço
        if (d.getCep() != null) {
            u.setCep(d.getCep());
        }

        if (d.getRua() != null) {
            u.setRua(d.getRua());
        }

        if (d.getNumero() != null) {
            u.setNumero(d.getNumero());
        }

        if (d.getComplemento() != null) {
            u.setComplemento(d.getComplemento());
        }

        if (d.getBairro() != null) {
            u.setBairro(d.getBairro());
        }

        if (d.getCidade() != null) {
            u.setCidade(d.getCidade());
        }

        if (d.getEstado() != null) {
            u.setEstado(d.getEstado());
        }

        return dto(repo.save(u));
    }

    public LoginResponseDTO login(LoginRequestDTO d) {

        if (d == null || d.getUsername() == null || d.getSenha() == null) {
            throw new RuntimeException("username e senha são obrigatórios");
        }

        Usuario u = repo.findByUsername(d.getUsername().trim())
                .orElseThrow(() -> new RuntimeException("Usuário ou senha inválidos"));

        if (!encoder.matches(d.getSenha(), u.getSenha())) {
            throw new RuntimeException("Usuário ou senha inválidos");
        }

        return new LoginResponseDTO(
                u.getIdUsuario(),
                u.getUsername(),
                u.getRole(),
                u.getNome(),
                u.getTelefone(),
                u.getFotoPerfil(),
                u.getCep(),
                u.getRua(),
                u.getNumero(),
                u.getComplemento(),
                u.getBairro(),
                u.getCidade(),
                u.getEstado()
        );
    }

    public void deletar(Long id) {
        repo.delete(entity(id));
    }

    private UsuarioResponseDTO dto(Usuario u) {

        UsuarioResponseDTO d = new UsuarioResponseDTO();

        d.setIdUsuario(u.getIdUsuario());
        d.setUsername(u.getUsername());
        d.setRole(u.getRole());
        d.setNome(u.getNome());
        d.setTelefone(u.getTelefone());
        d.setFotoPerfil(u.getFotoPerfil());

        // Retornar endereço
        d.setCep(u.getCep());
        d.setRua(u.getRua());
        d.setNumero(u.getNumero());
        d.setComplemento(u.getComplemento());
        d.setBairro(u.getBairro());
        d.setCidade(u.getCidade());
        d.setEstado(u.getEstado());

        return d;
    }
}