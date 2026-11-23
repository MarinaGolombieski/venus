package com.example.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.example.dtos.*;
import com.example.entities.*;

public final class DtoMapper {
    private DtoMapper() {}
    public static ProdutoResponseDTO produto(Produto p){
        ProdutoResponseDTO d=new ProdutoResponseDTO(); 
        d.setId(p.getId()); 
        d.setNome(p.getNome()); 
        d.setDescricao(p.getDescricao()); 
        d.setPreco(p.getPreco()); 
        d.setEstoque(p.getEstoque()); 
        d.setTipo(p.getTipo()); 
        d.setEstado(p.getEstado()); 
        d.setCategoriaId(p.getCategoria()==null?null:p.getCategoria().getIdCategoria()); 
        
        d.setTamanho(p.getTamanho());

        d.setImagemIds(
            p.getImagens() == null
                ? List.of()
                : p.getImagens().stream()
                    .map(Imagem::getIdImagem)
                    .toList()
        );

        d.setCor(p.getCor());
        d.setModelagem(p.getModelagem());
        d.setEstilo(p.getEstilo());

        return d;
    }
    public static PersonalizacaoResponseDTO personalizacao(Personalizacao p){
        PersonalizacaoResponseDTO d=new PersonalizacaoResponseDTO();
        d.setIdPersonalizacao(p.getIdPersonalizacao()); 
        d.setNivel(p.getNivel()); 
        d.setTipo(p.getTipo()); 
        d.setValor(p.getValor()); 
        d.setItemCarrinhoId(p.getItemCarrinho()==null?null:p.getItemCarrinho().getIdItemCarrinho());
        d.setItemPedidoId(p.getItemPedido()==null?null:p.getItemPedido().getIdItemPedido());
        if(p.getTipo()!=null){
            switch(p.getTipo()){
                case BORDADO_DESENHO -> { if(p.getIdPersonalizacao()!=null){} }
                default -> {}
            }
        }
        return d;
    }
    public static ItemCarrinhoResponseDTO itemCarrinho(ItemCarrinho i){
        ItemCarrinhoResponseDTO d=new ItemCarrinhoResponseDTO(); 
        d.setIdItemCarrinho(i.getIdItemCarrinho()); 
        d.setTamanho(i.getTamanho()); 
        d.setQuantidade(i.getQuantidade()); 
        d.setCarrinhoId(i.getCarrinho()==null?null:i.getCarrinho().getIdCarrinho());
        d.setProdutoId(i.getProduto()==null?null:i.getProduto().getId()); 
        d.setProdutoNome(i.getProduto()==null?null:i.getProduto().getNome());
        d.setPrecoProduto(i.getProduto()==null?null:i.getProduto().getPreco());
        List<PersonalizacaoResponseDTO> ps=i.getPersonalizacoes()==null?List.of():i.getPersonalizacoes().stream().map(DtoMapper::personalizacao).toList(); 
        d.setPersonalizacoes(ps);
        BigDecimal extras=ps.stream().map(PersonalizacaoResponseDTO::getValor).filter(x->x!=null).reduce(BigDecimal.ZERO,BigDecimal::add); 
        d.setValorPersonalizacoes(extras);
        BigDecimal unit=(i.getProduto()==null||i.getProduto().getPreco()==null?BigDecimal.ZERO:i.getProduto().getPreco()).add(extras); 
        d.setPrecoUnitarioFinal(unit);
        d.setSubtotal(unit.multiply(BigDecimal.valueOf(i.getQuantidade()))); 
        return d;
    }
    public static CarrinhoResponseDTO carrinho(Carrinho c){ CarrinhoResponseDTO d=new CarrinhoResponseDTO(); d.setIdCarrinho(c.getIdCarrinho()); 
    d.setUsuarioId(c.getUsuario()==null?null:c.getUsuario().getIdUsuario()); 
    List<ItemCarrinhoResponseDTO> itens=c.getItens()==null?List.of():c.getItens().stream().map(DtoMapper::itemCarrinho).toList(); 
    d.setItens(itens); 
    d.setValorTotal(itens.stream().map(ItemCarrinhoResponseDTO::getSubtotal).reduce(BigDecimal.ZERO,BigDecimal::add)); 
    return d; }
    public static ItemPedidoResponseDTO itemPedido(ItemPedido i){ ItemPedidoResponseDTO d=new ItemPedidoResponseDTO(); 
    d.setIdItemPedido(i.getIdItemPedido()); 
    d.setTamanho(i.getTamanho()); 
    d.setQuantidade(i.getQuantidade()); 
    d.setPrecoUnitario(i.getPrecoUnitario()); 
    d.setProdutoId(i.getProduto()==null?null:i.getProduto().getId()); 
    d.setPedidoId(i.getPedido()==null?null:i.getPedido().getIdPedido());
    d.setPersonalizacoes(i.getPersonalizacoes()==null?List.of():i.getPersonalizacoes().stream().map(DtoMapper::personalizacao).toList()); 
    return d; }
    public static PedidoResponseDTO pedido(Pedido p){ PedidoResponseDTO d=new PedidoResponseDTO(); 
    d.setIdPedido(p.getIdPedido()); 
    d.setValorTotal(p.getValorTotal()); 
    d.setStatus(p.getStatus()); 
    d.setUsuarioId(p.getUsuario()==null?null:p.getUsuario().getIdUsuario()); 
    d.setItens(p.getItens()==null?List.of():p.getItens().stream().map(DtoMapper::itemPedido).toList()); 
    d.setPagamentoId(p.getPagamento()==null?null:p.getPagamento().getIdPagamento()); 
    return d; }
    public static PagamentoResponseDTO pagamento(Pagamento p){ 
    	PagamentoResponseDTO d=new PagamentoResponseDTO(); 
    d.setIdPagamento(p.getIdPagamento()); 
    d.setValor(p.getValor());
    d.setMetodoPagamento(p.getMetodoPagamento()); 
    d.setStatus(p.getStatus()); 
    d.setPedidoId(p.getPedido()==null?null:p.getPedido().getIdPedido()); 
    return d; }
}
