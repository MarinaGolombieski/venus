package com.example.entities;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_carrinho")
public class Carrinho {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarrinho;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrinho> itens = new ArrayList<>();

    public Carrinho() {}
    public Long getIdCarrinho() { return idCarrinho; }
    public void setIdCarrinho(Long idCarrinho) { this.idCarrinho = idCarrinho; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public List<ItemCarrinho> getItens() { return itens; }
    public void setItens(List<ItemCarrinho> itens) { this.itens = itens; }
}
