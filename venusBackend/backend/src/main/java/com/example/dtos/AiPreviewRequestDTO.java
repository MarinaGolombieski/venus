package com.example.dtos;
import java.util.Map;
public class AiPreviewRequestDTO {
    private Long produtoId;
    private String tipoAlteracao;
    private Map<String,String> detalhes;
    public Long getProdutoId(){return produtoId;}
    public void setProdutoId(Long v){produtoId=v;}
    public String getTipoAlteracao(){return tipoAlteracao;}
    public void setTipoAlteracao(String v){tipoAlteracao=v;}
    public Map<String,String> getDetalhes(){return detalhes;}
    public void setDetalhes(Map<String,String> v){detalhes=v;}
}
