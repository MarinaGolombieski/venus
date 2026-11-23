package com.example.dtos;
import Enum.*;
public class PersonalizacaoRequestDTO {
    private Long itemCarrinhoId; private TipoAlteracao tipo;
    private Desenho desenho; private Cor cor; private TamanhoBordado tamanhoBordado; private LocalAplicacao localAplicacao;
    private String texto; private Fonte fonte;
    private TipoRenda tipoRenda; private LarguraRenda larguraRenda;
    private CorTingimento corTingimento; private TipoManga tipoManga;
    public PersonalizacaoRequestDTO() {}
    public Long getItemCarrinhoId(){return itemCarrinhoId;} public void setItemCarrinhoId(Long v){itemCarrinhoId=v;}
    public TipoAlteracao getTipo(){return tipo;} public void setTipo(TipoAlteracao v){tipo=v;}
    public Desenho getDesenho(){return desenho;} public void setDesenho(Desenho v){desenho=v;}
    public Cor getCor(){return cor;} public void setCor(Cor v){cor=v;}
    public TamanhoBordado getTamanhoBordado(){return tamanhoBordado;} public void setTamanhoBordado(TamanhoBordado v){tamanhoBordado=v;}
    public LocalAplicacao getLocalAplicacao(){return localAplicacao;} public void setLocalAplicacao(LocalAplicacao v){localAplicacao=v;}
    public String getTexto(){return texto;} public void setTexto(String v){texto=v;}
    public Fonte getFonte(){return fonte;} public void setFonte(Fonte v){fonte=v;}
    public TipoRenda getTipoRenda(){return tipoRenda;} public void setTipoRenda(TipoRenda v){tipoRenda=v;}
    public LarguraRenda getLarguraRenda(){return larguraRenda;} public void setLarguraRenda(LarguraRenda v){larguraRenda=v;}
    public CorTingimento getCorTingimento(){return corTingimento;} public void setCorTingimento(CorTingimento v){corTingimento=v;}
    public TipoManga getTipoManga(){return tipoManga;} public void setTipoManga(TipoManga v){tipoManga=v;}
}
