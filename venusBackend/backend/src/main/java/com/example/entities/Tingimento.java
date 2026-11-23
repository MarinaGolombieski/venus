package com.example.entities;
import Enum.*; import jakarta.persistence.*;
@Entity 
@Table(name="tb_tingimento")
public class Tingimento {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @OneToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="personalizacao_id",nullable=false,unique=true) private Personalizacao personalizacao;
 @Enumerated(EnumType.STRING) private CorTingimento cor;
 public Long getId(){return id;} public void setId(Long v){id=v;}
 public Personalizacao getPersonalizacao(){return personalizacao;} public void setPersonalizacao(Personalizacao v){personalizacao=v;}
 public CorTingimento getCor(){return cor;} public void setCor(CorTingimento v){cor=v;}
}
