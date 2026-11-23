package com.example.dtos;
import java.math.BigDecimal; import Enum.*;
public class PersonalizacaoResponseDTO {
    private Long idPersonalizacao; private NivelPersonalizacao nivel; private TipoAlteracao tipo; private BigDecimal valor; private Long itemCarrinhoId; private Long itemPedidoId;
    private Desenho desenho; private Cor cor; private TamanhoBordado tamanhoBordado; private LocalAplicacao localAplicacao; private String texto; private Fonte fonte;
    private TipoRenda tipoRenda; private LarguraRenda larguraRenda; private CorTingimento corTingimento; private TipoManga tipoManga;
    public PersonalizacaoResponseDTO() {}
    public Long getIdPersonalizacao(){return idPersonalizacao;} public void setIdPersonalizacao(Long v){idPersonalizacao=v;} public NivelPersonalizacao getNivel(){return nivel;} public void setNivel(NivelPersonalizacao v){nivel=v;}
    public TipoAlteracao getTipo(){return tipo;} public void setTipo(TipoAlteracao v){tipo=v;} public BigDecimal getValor(){return valor;} public void setValor(BigDecimal v){valor=v;}
    public Long getItemCarrinhoId(){return itemCarrinhoId;} public void setItemCarrinhoId(Long v){itemCarrinhoId=v;} public Long getItemPedidoId(){return itemPedidoId;} public void setItemPedidoId(Long v){itemPedidoId=v;}
    public Desenho getDesenho(){return desenho;} public void setDesenho(Desenho v){desenho=v;} public Cor getCor(){return cor;} public void setCor(Cor v){cor=v;}
    public TamanhoBordado getTamanhoBordado(){return tamanhoBordado;} public void setTamanhoBordado(TamanhoBordado v){tamanhoBordado=v;} public LocalAplicacao getLocalAplicacao(){return localAplicacao;} public void setLocalAplicacao(LocalAplicacao v){localAplicacao=v;}
    public String getTexto(){return texto;} public void setTexto(String v){texto=v;} public Fonte getFonte(){return fonte;} public void setFonte(Fonte v){fonte=v;}
    public TipoRenda getTipoRenda(){return tipoRenda;} public void setTipoRenda(TipoRenda v){tipoRenda=v;} public LarguraRenda getLarguraRenda(){return larguraRenda;} public void setLarguraRenda(LarguraRenda v){larguraRenda=v;}
    public CorTingimento getCorTingimento(){return corTingimento;} public void setCorTingimento(CorTingimento v){corTingimento=v;} public TipoManga getTipoManga(){return tipoManga;} public void setTipoManga(TipoManga v){tipoManga=v;}
}
