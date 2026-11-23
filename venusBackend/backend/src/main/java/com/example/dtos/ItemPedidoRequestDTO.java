package com.example.dtos;


public class ItemPedidoRequestDTO {

    private int quantidade;
    private Long produtoId;

    public ItemPedidoRequestDTO() {
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }
}