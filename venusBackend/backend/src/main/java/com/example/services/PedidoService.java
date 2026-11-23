package com.example.services;
import java.math.BigDecimal; 
import java.util.*; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional;
import Enum.StatusPedido; 
import com.example.dtos.*; 
import com.example.entities.*; 
import com.example.repositories.*; 
import com.example.utils.DtoMapper;
@Service
public class PedidoService {
 private final PedidoRepository pedidos; 
 private final CarrinhoRepository carrinhos; 
 private final PersonalizacaoService personalizacoes;
 public PedidoService(PedidoRepository pedidos,CarrinhoRepository carrinhos,PersonalizacaoService personalizacoes){
	 this.pedidos=pedidos;
	 this.carrinhos=carrinhos;
 this.personalizacoes=personalizacoes;
 }
 @Transactional(readOnly=true) public List<PedidoResponseDTO> listarTodos(){
	 return pedidos.findAll().stream().map(this::response).toList();
 }
 public Pedido entity(Long id){
	 return pedidos.findById(id).orElseThrow(()->new RuntimeException("Pedido não encontrado"));
	 }
 @Transactional(readOnly=true) public PedidoResponseDTO buscar(Long id){
	 return response(entity(id));
	 }
 @Transactional public PedidoResponseDTO checkout(PedidoRequestDTO d){
	 if(d==null||d.getCarrinhoId()==null)throw new RuntimeException("carrinhoId é obrigatório"); 
	 Carrinho c=carrinhos.findById(d.getCarrinhoId()).orElseThrow(()->new RuntimeException("Carrinho não encontrado")); 
	 if(c.getItens().isEmpty())throw new RuntimeException("O carrinho está vazio"); Pedido p=new Pedido();
	 p.setUsuario(c.getUsuario());p.setStatus(StatusPedido.PENDENTE);BigDecimal total=BigDecimal.ZERO;
  for(ItemCarrinho ci:c.getItens()){
	  Produto prod=ci.getProduto();
	  if(ci.getQuantidade()<=0)throw new RuntimeException("Quantidade inválida");
	  if(prod.getEstoque()<ci.getQuantidade())throw new RuntimeException("Estoque insuficiente para o produto: "+prod.getNome());
	  ItemPedido ip=new ItemPedido();
	  
	  ip.setPedido(p);
	  ip.setProduto(prod);
	  ip.setTamanho(ci.getTamanho());
	  ip.setQuantidade(ci.getQuantidade());
	  BigDecimal unit=prod.getPreco().add(personalizacoes.valor(ci.getPersonalizacoes()));
	  ip.setPrecoUnitario(unit);
	  p.getItens().add(ip);
	  prod.setEstoque(prod.getEstoque()-ci.getQuantidade());total=total.add(unit.multiply(BigDecimal.valueOf(ci.getQuantidade())));}
  p.setValorTotal(total); 
  Pedido salvo=pedidos.save(p);
  for(int n=0;n<c.getItens().size();n++){
	  ItemCarrinho ci=c.getItens().get(n); 
	  ItemPedido ip=salvo.getItens().get(n); 
	  for(Personalizacao pc:ci.getPersonalizacoes()) personalizacoes.copiarParaPedido(pc,ip); }
  
  

salvo = pedidos.save(salvo);

// Excluir as personalizações originais e seus detalhes
// antes de remover os itens do carrinho.
for (ItemCarrinho ci : new ArrayList<>(c.getItens())) {
    for (Personalizacao pc :
            new ArrayList<>(ci.getPersonalizacoes())) {
        personalizacoes.deletar(pc.getIdPersonalizacao());
    }
}

// Agora os itens podem ser removidos com segurança.
c.getItens().clear();
carrinhos.save(c);

return response(salvo);

  
  }
 public void cancelar(Long id){
	 Pedido p=entity(id);
 if(p.getStatus()!=StatusPedido.PENDENTE)throw new RuntimeException("Somente pedidos pendentes podem ser cancelados");
 restaurarEstoque(p);
 p.setStatus(StatusPedido.CANCELADO);pedidos.save(p);}
 public void cancelarPorPagamento(Pedido p){
	 if(p.getStatus()==StatusPedido.PENDENTE){
		 restaurarEstoque(p);
		 p.setStatus(StatusPedido.CANCELADO);
		 pedidos.save(p);}}
 private void restaurarEstoque(Pedido p){
	 if(p.getItens()==null)return;for(ItemPedido i:p.getItens()){
		 if(i.getProduto()!=null)i.getProduto().setEstoque(i.getProduto().getEstoque()+i.getQuantidade());}}
 public void atualizarStatus(Long id,StatusPedido status){
	 Pedido p=entity(id);
	 if(status==null)throw new RuntimeException("status é obrigatório");
	 StatusPedido atual=p.getStatus();
	 boolean ok=(atual==StatusPedido.PENDENTE&&(status==StatusPedido.PAGO||status==StatusPedido.CANCELADO))||(atual==StatusPedido.PAGO&&(status==StatusPedido.ENVIADO))||(atual==StatusPedido.ENVIADO&&(status==StatusPedido.ENTREGUE));
	 if(!ok)throw new RuntimeException("Transição de status inválida: "+atual+" -> "+status);
	 if(status==StatusPedido.CANCELADO)restaurarEstoque(p);
	 p.setStatus(status);pedidos.save(p);}
 public void deletar(Long id){
	 pedidos.delete(entity(id));}
 private PedidoResponseDTO response(Pedido p){
	 PedidoResponseDTO d=DtoMapper.pedido(p); 
	 if(p.getItens()!=null)d.setItens(p.getItens().stream().map(i->{
		 var x=DtoMapper.itemPedido(i); 
	 x.setPersonalizacoes(i.getPersonalizacoes()==null?List.of():i.getPersonalizacoes().stream().map(personalizacoes::toDto).toList()); 
	 return x;}).toList()); 
	 return d; }
}
