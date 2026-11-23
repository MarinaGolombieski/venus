package com.example.entities;

import java.math.BigDecimal;
import java.util.List;

import Enum.EstadoProduto;
import Enum.Tamanho;
import Enum.TipoProduto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_produto")
public class Produto {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	private String nome;
	
	 @Column(length = 1000)
	private String descricao;
	 
	private BigDecimal preco;
	private int estoque;
    

    @Enumerated(EnumType.STRING)
    private TipoProduto tipo;
    
    @Enumerated(EnumType.STRING)
    private EstadoProduto estado;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
    
    @Enumerated(EnumType.STRING)
    private Tamanho tamanho;
    

 // Novos parâmetros para recomendação
    @Column(length = 50)
    private String cor;

    @Column(length = 50)
    private String modelagem;

    @Column(length = 50)
    private String estilo;
    
    
    @OneToMany(mappedBy = "produto")
    private List<Imagem> imagens;
    
	@Lob
	private byte[] thumbnail;

	
	
	public Produto() {
		
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getNome() {
		return nome;
	}



	public void setNome(String nome) {
		this.nome = nome;
	}



	public String getDescricao() {
		return descricao;
	}



	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}



	public int getEstoque() {
		return estoque;
	}



	public void setEstoque(int estoque) {
		this.estoque = estoque;
	}



	public TipoProduto getTipo() {
		return tipo;
	}



	public void setTipo(TipoProduto tipo) {
		this.tipo = tipo;
	}



	public EstadoProduto getEstado() {
		return estado;
	}



	public void setEstado(EstadoProduto estado) {
		this.estado = estado;
	}



	public Categoria getCategoria() {
		return categoria;
	}



	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}



	public Tamanho getTamanho() {
		return tamanho;
	}



	public void setTamanho(Tamanho tamanho) {
		this.tamanho = tamanho;
	}



	public List<Imagem> getImagens() {
		return imagens;
	}



	public void setImagens(List<Imagem> imagens) {
		this.imagens = imagens;
	}



	public byte[] getThumbnail() {
		return thumbnail;
	}



	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}



	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}



	public BigDecimal getPreco() {
		return preco;
	}
	
	public String getCor() {
	    return cor;
	}

	public void setCor(String cor) {
	    this.cor = cor;
	}

	public String getModelagem() {
	    return modelagem;
	}

	public void setModelagem(String modelagem) {
	    this.modelagem = modelagem;
	}

	public String getEstilo() {
	    return estilo;
	}

	public void setEstilo(String estilo) {
	    this.estilo = estilo;
	}
}
	