package com.example.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_imagem")
	public class Imagem {
	
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long idImagem;

	    @Lob
	    private byte[] arquivo;

	    @ManyToOne
	    @JoinColumn(name = "produto_id")
	    private Produto produto;
	    
	    private String contentType;

    public Imagem() {
    }

	public Long getIdImagem() {
		return idImagem;
	}

	public void setIdImagem(Long idImagem) {
		this.idImagem = idImagem;
	}

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

	public String getContentType() { return contentType; }
	public void setContentType(String contentType) { this.contentType = contentType; }

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}
    
    

}

