package com.example.entities;
import Enum.*; import jakarta.persistence.*;
@Entity
@Table(name="tb_aplicacao_renda")
public class AplicacaoRenda {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @OneToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="personalizacao_id",nullable=false,unique=true) private Personalizacao personalizacao;
 @Enumerated(EnumType.STRING) private TipoRenda tipoRenda;
 @Enumerated(EnumType.STRING) private Cor cor;
 @Enumerated(EnumType.STRING) private LarguraRenda largura;
 public Long getId(){return id;} public void setId(Long v){id=v;}
 public Personalizacao getPersonalizacao(){return personalizacao;} public void setPersonalizacao(Personalizacao v){personalizacao=v;}
 public TipoRenda getTipoRenda(){return tipoRenda;} public void setTipoRenda(TipoRenda v){tipoRenda=v;}
 public Cor getCor(){return cor;} public void setCor(Cor v){cor=v;}
 public LarguraRenda getLargura(){return largura;} public void setLargura(LarguraRenda v){largura=v;}
}
