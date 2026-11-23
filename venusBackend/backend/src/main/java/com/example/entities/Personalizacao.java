package com.example.entities;

import java.math.BigDecimal;

import Enum.NivelPersonalizacao;
import Enum.TipoAlteracao;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_personalizacao")
public class Personalizacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPersonalizacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelPersonalizacao nivel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAlteracao tipo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_carrinho_id")
    private ItemCarrinho itemCarrinho;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_pedido_id")
    private ItemPedido itemPedido;

    public Personalizacao() {}

    public Long getIdPersonalizacao() { return idPersonalizacao; }
    public void setIdPersonalizacao(Long idPersonalizacao) { this.idPersonalizacao = idPersonalizacao; }
    public NivelPersonalizacao getNivel() { return nivel; }
    public void setNivel(NivelPersonalizacao nivel) { this.nivel = nivel; }
    public TipoAlteracao getTipo() { return tipo; }
    public void setTipo(TipoAlteracao tipo) { this.tipo = tipo; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public ItemCarrinho getItemCarrinho() { return itemCarrinho; }
    public void setItemCarrinho(ItemCarrinho itemCarrinho) { this.itemCarrinho = itemCarrinho; }
    public ItemPedido getItemPedido() { return itemPedido; }
    public void setItemPedido(ItemPedido itemPedido) { this.itemPedido = itemPedido; }
}
