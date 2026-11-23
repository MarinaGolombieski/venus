package com.example.dtos;

import java.math.BigDecimal;

import Enum.*;

public class ProdutoRequestDTO {

    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int estoque;

    private TipoProduto tipo;
    private EstadoProduto estado;
    private Long categoriaId;
    private Tamanho tamanho;

    // Novos parâmetros para recomendação
    private String cor;
    private String modelagem;
    private String estilo;

    public ProdutoRequestDTO() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String v) {
        nome = v;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String v) {
        descricao = v;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal v) {
        preco = v;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int v) {
        estoque = v;
    }

    public TipoProduto getTipo() {
        return tipo;
    }

    public void setTipo(TipoProduto v) {
        tipo = v;
    }

    public EstadoProduto getEstado() {
        return estado;
    }

    public void setEstado(EstadoProduto v) {
        estado = v;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long v) {
        categoriaId = v;
    }

    public Tamanho getTamanho() {
        return tamanho;
    }

    public void setTamanho(Tamanho v) {
        tamanho = v;
    }

    // Getters e setters dos novos campos
    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getModelagem() {
        return modelagem;
    }

    public void setModelagem(String modelagem) {
        this.modelagem = modelagem;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }
}