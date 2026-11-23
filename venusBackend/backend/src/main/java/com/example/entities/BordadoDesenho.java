package com.example.entities;
import Enum.*; import jakarta.persistence.*;
@Entity 
@Table(name="tb_bordado_desenho")
public class BordadoDesenho {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @OneToOne(fetch=FetchType.LAZY, optional=false) 
 
 @JoinColumn(name="personalizacao_id",nullable=false,unique=true) 
 private Personalizacao personalizacao;
 @Enumerated(EnumType.STRING) private Desenho desenho;
 @Enumerated(EnumType.STRING) private Cor cor;
 @Enumerated(EnumType.STRING) private TamanhoBordado tamanho;
 @Enumerated(EnumType.STRING) private LocalAplicacao localAplicacao;
 public Long getId(){return id;} public void setId(Long v){id=v;}
 
 public Personalizacao getPersonalizacao(){
	 return personalizacao;
	 }
 public void setPersonalizacao(Personalizacao v){personalizacao=v;}
 public Desenho getDesenho(){return desenho;} public void setDesenho(Desenho v){desenho=v;}
 public Cor getCor(){return cor;} public void setCor(Cor v){cor=v;}
 public TamanhoBordado getTamanho(){return tamanho;} public void setTamanho(TamanhoBordado v){tamanho=v;}
 public LocalAplicacao getLocalAplicacao(){return localAplicacao;} public void setLocalAplicacao(LocalAplicacao v){localAplicacao=v;}
}
