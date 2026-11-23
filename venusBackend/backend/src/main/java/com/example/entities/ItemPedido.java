package com.example.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import Enum.Tamanho;

@Entity
@Table(name = "tb_item_pedido")
public class ItemPedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItemPedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tamanho")
    private Tamanho tamanho;

    @Column(nullable = false)
    private int quantidade;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @OneToMany(mappedBy = "itemPedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Personalizacao> personalizacoes = new ArrayList<>();

    public Long getIdItemPedido(){ return idItemPedido; }
    public void setIdItemPedido(Long id){ this.idItemPedido = id; }
    public Pedido getPedido(){ return pedido; }
    public void setPedido(Pedido p){ this.pedido = p; }
    public Produto getProduto(){ return produto; }
    public void setProduto(Produto p){ this.produto = p; }
    public Tamanho getTamanho(){ return tamanho; }
    public void setTamanho(Tamanho t){ this.tamanho = t; }
    public int getQuantidade(){ return quantidade; }
    public void setQuantidade(int q){ this.quantidade = q; }
    public BigDecimal getPrecoUnitario(){ return precoUnitario; }
    public void setPrecoUnitario(BigDecimal p){ this.precoUnitario = p; }
    public List<Personalizacao> getPersonalizacoes(){ return personalizacoes; }
    public void setPersonalizacoes(List<Personalizacao> p){ this.personalizacoes = p; }
}
