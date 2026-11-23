package com.example.entities;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import Enum.Tamanho;

@Entity
@Table(name = "tb_item_carrinho")
public class ItemCarrinho {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItemCarrinho;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "carrinho_id", nullable = false)
    private Carrinho carrinho;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tamanho")
    private Tamanho tamanho;

    @Column(nullable = false)
    private int quantidade;

    @OneToMany(mappedBy = "itemCarrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Personalizacao> personalizacoes = new ArrayList<>();

    public Long getIdItemCarrinho(){ return idItemCarrinho; }
    public void setIdItemCarrinho(Long id){ this.idItemCarrinho = id; }
    public Carrinho getCarrinho(){ return carrinho; }
    public void setCarrinho(Carrinho c){ this.carrinho = c; }
    public Produto getProduto(){ return produto; }
    public void setProduto(Produto p){ this.produto = p; }
    public Tamanho getTamanho(){ return tamanho; }
    public void setTamanho(Tamanho t){ this.tamanho = t; }
    public int getQuantidade(){ return quantidade; }
    public void setQuantidade(int q){ this.quantidade = q; }
    public List<Personalizacao> getPersonalizacoes(){ return personalizacoes; }
    public void setPersonalizacoes(List<Personalizacao> p){ this.personalizacoes = p; }
}
