package com.example.dtos;

import Enum.Role;

public class LoginResponseDTO {

    private Long idUsuario;
    private String username;
    private Role role;
    private String nome;
    private String telefone;
    private String fotoPerfil;

    // Dados do endereço
    private String cep;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(
            Long id,
            String username,
            Role role,
            String nome,
            String telefone,
            String fotoPerfil,
            String cep,
            String rua,
            String numero,
            String complemento,
            String bairro,
            String cidade,
            String estado
    ) {
        this.idUsuario = id;
        this.username = username;
        this.role = role;
        this.nome = nome;
        this.telefone = telefone;
        this.fotoPerfil = fotoPerfil;

        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public String getCep() {
        return cep;
    }

    public String getRua() {
        return rua;
    }

    public String getNumero() {
        return numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }
}