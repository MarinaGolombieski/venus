package com.example.services;
import java.util.List; 
import org.springframework.stereotype.Service; 
import org.springframework.web.multipart.MultipartFile; 
import com.example.dtos.*; 
import com.example.entities.*; 
import com.example.repositories.*;

@Service 
public class ImagemService {
	private final ImagemRepository repo;
	private final ProdutoRepository produtos;
	public ImagemService(ImagemRepository repo,ProdutoRepository produtos){
		this.repo=repo;this.produtos=produtos;
		}
	public List<ImagemResponseDTO> listarTodos(){
		return repo.findAll().stream().map(this::dto).toList();
		}
	public Imagem buscar(Long id){
		return repo.findById(id).orElseThrow(()->new RuntimeException("Imagem não encontrada"));
		}
	public ImagemResponseDTO salvar(ImagemRequestDTO d){
		if(d==null||d.getProdutoId()==null||d.getArquivo()==null||d.getArquivo().length==0)throw new RuntimeException("produtoId e arquivo são obrigatórios");
		Imagem i=new Imagem();
		i.setProduto(produtos.findById(d.getProdutoId()).orElseThrow(()->new RuntimeException("Produto não encontrado")));
		i.setArquivo(d.getArquivo());
		i.setContentType(d.getContentType()==null?"application/octet-stream":d.getContentType());
		return dto(repo.save(i));
		}
	public ImagemResponseDTO salvarMultipart(Long produtoId, MultipartFile arquivo){
		try{
			if(arquivo==null||arquivo.isEmpty())throw new RuntimeException("Arquivo é obrigatório");
			ImagemRequestDTO d=new ImagemRequestDTO();
			d.setProdutoId(produtoId);
			d.setArquivo(arquivo.getBytes());
			return salvar(d);
			}
		catch(java.io.IOException e){
			throw new RuntimeException("Não foi possível ler a imagem");
			}
		}
	public void deletar(Long id){
		repo.delete(buscar(id));
		}
	private ImagemResponseDTO dto(Imagem i){
			ImagemResponseDTO d=new ImagemResponseDTO();
			d.setIdImagem(i.getIdImagem());
			d.setProdutoId(i.getProduto()==null?null:i.getProduto().getId());
			return d;
			}
	}
