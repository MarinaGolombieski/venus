package com.example.entities;
import Enum.*; import jakarta.persistence.*;
@Entity 
@Table(name="tb_bordado_escrita")
public class BordadoEscrita {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @OneToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="personalizacao_id",nullable=false,unique=true) private Personalizacao personalizacao;
 @Column(nullable=false,length=100) private String texto;
 @Enumerated(EnumType.STRING) private Fonte fonte;
 @Enumerated(EnumType.STRING) private Cor cor;
 @Enumerated(EnumType.STRING) private TamanhoBordado tamanho;
 public Long getId(){return id;} public void setId(Long v){id=v;}
 public Personalizacao getPersonalizacao(){return personalizacao;} public void setPersonalizacao(Personalizacao v){personalizacao=v;}
 public String getTexto(){return texto;} public void setTexto(String v){texto=v;}
 public Fonte getFonte(){return fonte;} public void setFonte(Fonte v){fonte=v;}
 public Cor getCor(){return cor;} public void setCor(Cor v){cor=v;}
 public TamanhoBordado getTamanho(){return tamanho;} public void setTamanho(TamanhoBordado v){tamanho=v;}
}
