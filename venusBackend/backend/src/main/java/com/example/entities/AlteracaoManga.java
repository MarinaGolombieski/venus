package com.example.entities;
import Enum.*; import jakarta.persistence.*;
@Entity 
@Table(name="tb_alteracao_manga")
public class AlteracaoManga {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @OneToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="personalizacao_id",nullable=false,unique=true) private Personalizacao personalizacao;
 @Enumerated(EnumType.STRING) private TipoManga tipoManga;
 public Long getId(){return id;} public void setId(Long v){id=v;}
 public Personalizacao getPersonalizacao(){return personalizacao;} public void setPersonalizacao(Personalizacao v){personalizacao=v;}
 public TipoManga getTipoManga(){return tipoManga;} public void setTipoManga(TipoManga v){tipoManga=v;}
}
