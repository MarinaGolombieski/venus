package com.example.services;
import java.util.List; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional;
import com.example.dtos.*; 
import com.example.entities.*; 
import com.example.repositories.*; 
import com.example.utils.DtoMapper;
@Service
public class ProdutoService {
 private final ProdutoRepository repo; 
 private final CategoriaRepository categorias;
 public ProdutoService(ProdutoRepository repo,CategoriaRepository categorias){
	 this.repo=repo;
	 this.categorias=categorias;
	 }
 @Transactional(readOnly=true) 
 public List<ProdutoResponseDTO> listarTodos(){
	 return repo.findAll().stream().map(DtoMapper::produto).toList();}
 @Transactional(readOnly=true)
 public ProdutoResponseDTO buscarPorId(Long id){
	 return DtoMapper.produto(entity(id));}
 public Produto entity(Long id){
	 return repo.findById(id).orElseThrow(()->new RuntimeException("Produto não encontrado"));}
 public ProdutoResponseDTO salvar(ProdutoRequestDTO d){
	 validate(d); 
	 Produto p=new Produto();
	 apply(p,d);
	 return DtoMapper.produto(repo.save(p));}
 public ProdutoResponseDTO atualizar(Long id,ProdutoRequestDTO d){
	 validate(d); 
	 Produto p=entity(id); 
	 apply(p,d);
	 return DtoMapper.produto(repo.save(p));}
 private void validate(ProdutoRequestDTO d){
	 if(d==null||d.getNome()==null||d.getNome().isBlank())throw new RuntimeException("O nome do produto é obrigatório"); 
	 if(d.getPreco()==null||d.getPreco().signum()<0)throw new RuntimeException("O preço do produto é inválido"); 
	 if(d.getEstoque()<0)throw new RuntimeException("O estoque não pode ser negativo");
	 if(d.getTipo()==null)throw new RuntimeException("O tipo do produto é obrigatório");}
 
 
 
 private void apply(Produto p, ProdutoRequestDTO d) {
	    p.setNome(d.getNome().trim());
	    p.setDescricao(d.getDescricao());
	    p.setPreco(d.getPreco());
	    p.setEstoque(d.getEstoque());
	    p.setTipo(d.getTipo());
	    p.setEstado(d.getEstado());
	    p.setTamanho(d.getTamanho());

	    p.setCategoria(
	        d.getCategoriaId() == null
	            ? null
	            : categorias.findById(d.getCategoriaId())
	                .orElseThrow(() ->
	                    new RuntimeException("Categoria não encontrada")
	                )
	    );

	    p.setCor(d.getCor());
	    p.setModelagem(d.getModelagem());
	    p.setEstilo(d.getEstilo());
	}
 
 @Transactional(readOnly = true)
 public List<ProdutoResponseDTO> recomendar(Long id) {

     Produto produtoBase = entity(id);

     return repo.findAll().stream()
         .filter(p -> !p.getId().equals(produtoBase.getId()))
         .map(p -> new Object[] {
             p,
             pontuar(produtoBase, p)
         })
         .filter(item -> (int) item[1] > 0)
         .sorted((a, b) ->
             Integer.compare((int) b[1], (int) a[1])
         )
         .map(item -> DtoMapper.produto((Produto) item[0]))
         .limit(5)
         .toList();
 }
 
 private int pontuar(Produto base, Produto outro) {

	    int pontos = 0;

	    if (base.getCor() != null &&
	        base.getCor().equalsIgnoreCase(outro.getCor())) {
	        pontos++;
	    }

	    if (base.getModelagem() != null &&
	        base.getModelagem().equalsIgnoreCase(outro.getModelagem())) {
	        pontos++;
	    }

	    if (base.getCategoria() != null &&
	        outro.getCategoria() != null &&
	        base.getCategoria().getIdCategoria()
	            .equals(outro.getCategoria().getIdCategoria())) {
	        pontos++;
	    }

	    if (base.getEstilo() != null &&
	        base.getEstilo().equalsIgnoreCase(outro.getEstilo())) {
	        pontos++;
	    }

	    return pontos;
	}
 
 public void deletar(Long id){
	 repo.delete(entity(id));}
}
