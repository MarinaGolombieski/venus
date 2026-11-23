package com.example.services;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import com.example.dtos.*; import com.example.entities.*; import com.example.repositories.*; import com.example.utils.DtoMapper;
@Service
public class CarrinhoService {
 private final CarrinhoRepository repo;
 private final UsuarioRepository usuarios;
 private final ProdutoRepository produtos;
 private final PersonalizacaoService personalizacoes;
 private final ItemCarrinhoRepository itens;
 public CarrinhoService(
CarrinhoRepository repo,
UsuarioRepository usuarios,
ProdutoRepository produtos,
PersonalizacaoService personalizacoes,
ItemCarrinhoRepository itens
){
this.repo=repo;
this.usuarios=usuarios;
this.produtos=produtos;
this.personalizacoes=personalizacoes;
this.itens = itens;
}
 @Transactional(readOnly=true) public List<CarrinhoResponseDTO> listarTodos(){return repo.findAll().stream().map(this::response).toList();
 }
 @Transactional(readOnly=true) public CarrinhoResponseDTO buscar(Long id){
	 return response(entity(id));
	 }
 public CarrinhoResponseDTO buscarPorUsuario(Long usuarioId){return repo.findByUsuario_IdUsuario(usuarioId).map(this::response).orElseThrow(()->new RuntimeException("Carrinho não encontrado para o usuário"));
 }
 public Carrinho entity(Long id){
	 return repo.findById(id).orElseThrow(()->new RuntimeException("Carrinho não encontrado"));
	 }
 public CarrinhoResponseDTO criar(CarrinhoRequestDTO d){
	 if(d==null||d.getUsuarioId()==null)throw new RuntimeException("usuarioId é obrigatório"); 
	 if(repo.findByUsuario_IdUsuario(d.getUsuarioId()).isPresent())throw new RuntimeException("O usuário já possui um carrinho"); 
 Carrinho c=new Carrinho(); 
 c.setUsuario(usuarios.findById(d.getUsuarioId()).orElseThrow(()->new RuntimeException("Usuário não encontrado"))); 
 return response(repo.save(c));
 }
 @Transactional public ItemCarrinhoResponseDTO adicionarItem(ItemCarrinhoRequestDTO d){
	 if(d==null||d.getCarrinhoId()==null||d.getProdutoId()==null)throw new RuntimeException("carrinhoId e produtoId são obrigatórios"); 
	 if(d.getQuantidade()<=0)throw new RuntimeException("A quantidade deve ser maior que zero"); 
	 Carrinho c=entity(d.getCarrinhoId()); 
	 Produto p=produtos.findById(d.getProdutoId()).orElseThrow(()->new RuntimeException("Produto não encontrado")); 
	 if(p.getEstoque()<d.getQuantidade())throw new RuntimeException("Estoque insuficiente para o produto: "+p.getNome()); 
	 ItemCarrinho i=new ItemCarrinho();
	 i.setCarrinho(c);
	 i.setProduto(p);
	 i.setTamanho(d.getTamanho());
	 i.setQuantidade(d.getQuantidade());
	 
	 
	 c.getItens().add(i);

	 ItemCarrinho itemSalvo = itens.saveAndFlush(i);

	 System.out.println("ID após salvar item: " + itemSalvo.getIdItemCarrinho());

	 return itemResponse(itemSalvo);
 
 }
 @Transactional public ItemCarrinhoResponseDTO atualizarItem(Long id,int quantidade){
	 if(quantidade<=0)throw new RuntimeException("A quantidade deve ser maior que zero"); 
 ItemCarrinho i=findItem(id); 
 if(i.getProduto().getEstoque()<quantidade)throw new RuntimeException("Estoque insuficiente"); 
 i.setQuantidade(quantidade);return itemResponse(i);
 }
 
 
 @Transactional
 public void removerItem(Long id) {
     ItemCarrinho item = findItem(id);

     if (item.getPersonalizacoes() != null) {
         for (Personalizacao p :
                 new ArrayList<>(item.getPersonalizacoes())) {
             personalizacoes.deletar(p.getIdPersonalizacao());
         }
     }

     item.getCarrinho().getItens().remove(item);

     itens.delete(item);
     itens.flush();
 }
 
 
 
 public ItemCarrinho findItem(Long id){
return itens.findById(id)
.orElseThrow(()->new RuntimeException("Item do carrinho não encontrado"));
}
 @Transactional public CarrinhoResponseDTO atualizarResumo(Long id){return response(entity(id));
 }
 private CarrinhoResponseDTO response(Carrinho c){ CarrinhoResponseDTO d=new CarrinhoResponseDTO(); 
 d.setIdCarrinho(c.getIdCarrinho()); 
 d.setUsuarioId(c.getUsuario()==null?null:c.getUsuario().getIdUsuario()); 
 var itensDto=c.getItens()==null?List.<ItemCarrinhoResponseDTO>of():c.getItens().stream().map(this::itemResponse).toList(); 
 d.setItens(itensDto); d.setValorTotal(itensDto.stream().map(ItemCarrinhoResponseDTO::getSubtotal).reduce(BigDecimal.ZERO,BigDecimal::add)); 
 return d;
 }
 private ItemCarrinhoResponseDTO itemResponse(ItemCarrinho i){
	 ItemCarrinhoResponseDTO d=DtoMapper.itemCarrinho(i); 
	 d.setPersonalizacoes(i.getPersonalizacoes()==null?List.of():i.getPersonalizacoes().stream().map(personalizacoes::toDto).toList()); 
	 BigDecimal extras=personalizacoes.valor(i.getPersonalizacoes()); 
	 BigDecimal base=i.getProduto().getPreco(); 
	 BigDecimal unit=base.add(extras); 
	 d.setValorPersonalizacoes(extras);
	 d.setPrecoUnitarioFinal(unit); 
	 d.setSubtotal(unit.multiply(BigDecimal.valueOf(i.getQuantidade()))); 
	 return d; 
	 }
}