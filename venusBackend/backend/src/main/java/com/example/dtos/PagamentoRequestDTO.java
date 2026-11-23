package com.example.dtos;
import Enum.MetodoPagamento;
public class PagamentoRequestDTO { private MetodoPagamento metodoPagamento; private Long pedidoId; public PagamentoRequestDTO(){} public MetodoPagamento getMetodoPagamento(){return metodoPagamento;} public void setMetodoPagamento(MetodoPagamento v){metodoPagamento=v;} public Long getPedidoId(){return pedidoId;} public void setPedidoId(Long v){pedidoId=v;} }
