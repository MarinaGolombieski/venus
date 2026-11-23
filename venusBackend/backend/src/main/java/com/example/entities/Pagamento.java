package com.example.entities;

import Enum.MetodoPagamento;
import Enum.StatusPagamento;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_pagamento")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPagamento;

    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;

    @Enumerated(EnumType.STRING)
    private StatusPagamento status;

    @OneToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    public Pagamento() {
    }

    public Pagamento(Long idPagamento,
                     BigDecimal valor,
                     MetodoPagamento metodoPagamento,
                     StatusPagamento status,
                     Pedido pedido) {

        this.idPagamento = idPagamento;
        this.valor = valor;
        this.metodoPagamento = metodoPagamento;
        this.status = status;
        this.pedido = pedido;
    }

    public Long getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(Long idPagamento) {
        this.idPagamento = idPagamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }
    
    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public void setStatus(StatusPagamento status) {
        this.status = status;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}

                                                                                                                                              