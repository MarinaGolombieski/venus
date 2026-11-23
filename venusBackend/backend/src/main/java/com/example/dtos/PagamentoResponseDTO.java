package com.example.dtos;

import java.math.BigDecimal;

import Enum.MetodoPagamento;
import Enum.StatusPagamento;

public class PagamentoResponseDTO {
	 private Long idPagamento;
	    private BigDecimal valor;
	    private MetodoPagamento metodoPagamento;
	    private StatusPagamento status;
	    private Long pedidoId;

	    public PagamentoResponseDTO() {
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

	    public Long getPedidoId() {
	        return pedidoId;
	    }

	    public void setPedidoId(Long pedidoId) {
	        this.pedidoId = pedidoId;
	    }
}
