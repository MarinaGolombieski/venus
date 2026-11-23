package com.example.dtos;
import Enum.Tamanho;
public class ItemCarrinhoRequestDTO {
 private int quantidade; private Long carrinhoId; private Long produtoId; private Tamanho tamanho;
 public ItemCarrinhoRequestDTO(){}
 public int getQuantidade(){return quantidade;} public void setQuantidade(int v){quantidade=v;}
 public Long getCarrinhoId(){return carrinhoId;} public void setCarrinhoId(Long v){carrinhoId=v;}
 public Long getProdutoId(){return produtoId;} public void setProdutoId(Long v){produtoId=v;}
 public Tamanho getTamanho(){return tamanho;} public void setTamanho(Tamanho v){tamanho=v;}
}
