package com.example.dtos;

public class CategoriaResponseDTO {

	    private Long idCategoria;
	    private String nome;

	    public CategoriaResponseDTO() {
	    }

	    public Long getIdCategoria() {
	        return idCategoria;
	    }

	    public void setIdCategoria(Long idCategoria) {
	        this.idCategoria = idCategoria;
	    }

	    public String getNome() {
	        return nome;
	    }

	    public void setNome(String nome) {
	        this.nome = nome;
	    }
}
