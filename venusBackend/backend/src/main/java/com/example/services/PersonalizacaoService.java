package com.example.services;
import java.math.BigDecimal; 
import java.util.*; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional;
import Enum.*; 
import com.example.dtos.*; 
import com.example.entities.*; 
import com.example.repositories.*;
@Service
public class PersonalizacaoService {
 private static final int MAX=5;
 private final PersonalizacaoRepository repo; 
 private final ItemCarrinhoRepository itens; 
 private final BordadoDesenhoRepository desenhos; 
 private final BordadoEscritaRepository escritas; 
 private final AplicacaoRendaRepository rendas; 
 private final TingimentoRepository tingimentos; 
 private final AlteracaoMangaRepository mangas;
 private static final Map<TipoAlteracao,BigDecimal> PRECO=Map.of(TipoAlteracao.BORDADO_DESENHO,new BigDecimal("5.00"),TipoAlteracao.BORDADO_ESCRITA,new BigDecimal("5.00"),TipoAlteracao.TINGIMENTO,new BigDecimal("10.00"),TipoAlteracao.APLICACAO_RENDA,new BigDecimal("12.00"),TipoAlteracao.ALTERACAO_MANGA,new BigDecimal("18.00"));
 public PersonalizacaoService(PersonalizacaoRepository repo,ItemCarrinhoRepository itens,BordadoDesenhoRepository desenhos,BordadoEscritaRepository escritas,AplicacaoRendaRepository rendas,TingimentoRepository tingimentos,AlteracaoMangaRepository mangas){
	 this.repo=repo;
	 this.itens=itens;
	 this.desenhos=desenhos;
 this.escritas=escritas;
 this.rendas=rendas;
 this.tingimentos=tingimentos;
 this.mangas=mangas;
 }
 public List<PersonalizacaoResponseDTO> listarTodos(){
	 return repo.findAll().stream().map(this::toDto).toList();
	 }
 public Personalizacao entity(Long id){return repo.findById(id).orElseThrow(()->new RuntimeException("Personalização não encontrada"));
 }
 public PersonalizacaoResponseDTO buscar(Long id){
	 return toDto(entity(id));
	 }
 
 
 @Transactional 
 public PersonalizacaoResponseDTO adicionar(PersonalizacaoRequestDTO d){
	 if(d==null||d.getItemCarrinhoId()==null||d.getTipo()==null)throw new RuntimeException("itemCarrinhoId e tipo são obrigatórios");
	 ItemCarrinho item=itens.findById(d.getItemCarrinhoId()).orElseThrow(()->new RuntimeException("Item do carrinho não encontrado")); 
	 if(item.getProduto().getTipo()!=TipoProduto.SEMINOVO)throw new RuntimeException("Somente produtos seminovos podem ser personalizados"); 
	 if(item.getPersonalizacoes().size()>=MAX)throw new RuntimeException("O item pode ter no máximo 5 personalizações"); 
	 validateDetails(d); 
	 
	 
	 
	 Personalizacao p=new Personalizacao();
	 p.setItemCarrinho(item);
	 p.setTipo(d.getTipo());
	 p.setNivel(nivel(d.getTipo()));
	 p.setValor(PRECO.get(d.getTipo()));
	 p = repo.saveAndFlush(p);
	 saveDetail(p, d);
	 item.getPersonalizacoes().add(p); return toDto(p);}
 private void validateDetails(PersonalizacaoRequestDTO d){
	 switch(d.getTipo()){
	 case BORDADO_DESENHO->{
		 req(d.getDesenho(),"desenho");
		 req(d.getCor(),"cor");
		 req(d.getTamanhoBordado(),"tamanhoBordado");
		 req(d.getLocalAplicacao(),"localAplicacao");
		 }
	 case BORDADO_ESCRITA->{
		 if(d.getTexto()==null||d.getTexto().isBlank()||d.getTexto().length()>100)throw new RuntimeException("texto deve ter de 1 a 100 caracteres");
		 req(d.getFonte(),"fonte");
		 req(d.getCor(),"cor");
		 req(d.getTamanhoBordado(),"tamanhoBordado");
		 }
	 case APLICACAO_RENDA->{req(d.getTipoRenda(),"tipoRenda");
		 req(d.getCor(),"cor");
		 req(d.getLarguraRenda(),"larguraRenda");}case TINGIMENTO->req(d.getCorTingimento(),"corTingimento");case ALTERACAO_MANGA->req(d.getTipoManga(),"tipoManga");}}
 private void req(Object v,String n){if(v==null)throw new RuntimeException(n+" é obrigatório");
 }
 private NivelPersonalizacao nivel(TipoAlteracao t) {
	    return (t == TipoAlteracao.BORDADO_DESENHO
	            || t == TipoAlteracao.BORDADO_ESCRITA)
	            ? NivelPersonalizacao.DETALHE
	            : NivelPersonalizacao.TRANSFORMACAO;
	}
 private void saveDetail(Personalizacao p,PersonalizacaoRequestDTO d){
	 switch(d.getTipo()){
	 case BORDADO_DESENHO->{BordadoDesenho x=new BordadoDesenho();
	 x.setPersonalizacao(p);
	 x.setDesenho(d.getDesenho());
	 x.setCor(d.getCor());
	 x.setTamanho(d.getTamanhoBordado());
	 x.setLocalAplicacao(d.getLocalAplicacao());
	 desenhos.saveAndFlush(x);
	 }
	 
	 case BORDADO_ESCRITA->{
		 BordadoEscrita x=new BordadoEscrita();
		 x.setPersonalizacao(p);
		 x.setTexto(d.getTexto().trim());
		 x.setFonte(d.getFonte());
		 x.setCor(d.getCor());
		 x.setTamanho(d.getTamanhoBordado());
		 escritas.saveAndFlush(x);
		 }
	 case APLICACAO_RENDA->{
		 AplicacaoRenda x=new AplicacaoRenda();
		 x.setPersonalizacao(p);
		 x.setTipoRenda(d.getTipoRenda());
		 x.setCor(d.getCor());
		 x.setLargura(d.getLarguraRenda());
		 rendas.saveAndFlush(x);
		 }
	 case TINGIMENTO->{
		 Tingimento x=new Tingimento();
		 x.setPersonalizacao(p);
		 x.setCor(d.getCorTingimento());
		 tingimentos.saveAndFlush(x);
		 }
	 
	 case ALTERACAO_MANGA->{
		 AlteracaoManga x=new AlteracaoManga();
		 x.setPersonalizacao(p);
		 x.setTipoManga(d.getTipoManga());
		 mangas.saveAndFlush(x);
		 }}}
 

@Transactional
public void deletar(Long id) {
    Personalizacao p = entity(id);

    // Primeiro, excluir o detalhe que possui a chave estrangeira.
    excluirDetalhe(p);

    // Desvincular a personalização das listas dos itens.
    if (p.getItemCarrinho() != null) {
        p.getItemCarrinho().getPersonalizacoes().remove(p);
    }

    if (p.getItemPedido() != null) {
        p.getItemPedido().getPersonalizacoes().remove(p);
    }

    // Por fim, excluir a personalização.
    repo.delete(p);
    repo.flush();
}

 
 public BigDecimal valor(List<Personalizacao> p){return p==null?BigDecimal.ZERO:p.stream().map(Personalizacao::getValor).reduce(BigDecimal.ZERO,BigDecimal::add);}
 public PersonalizacaoResponseDTO toDto(Personalizacao p){
	 PersonalizacaoResponseDTO d=new PersonalizacaoResponseDTO();
	 d.setIdPersonalizacao(p.getIdPersonalizacao());
	 d.setNivel(p.getNivel());
	 d.setTipo(p.getTipo());
	 d.setValor(p.getValor());
	 d.setItemCarrinhoId(p.getItemCarrinho()==null?null:p.getItemCarrinho().getIdItemCarrinho());
	 d.setItemPedidoId(p.getItemPedido()==null?null:p.getItemPedido().getIdItemPedido());
	 Long id=p.getIdPersonalizacao(); 
	 if(id!=null)switch(p.getTipo()){
	 case BORDADO_DESENHO->desenhos.findByPersonalizacao_IdPersonalizacao(id).ifPresent(x->{
		 d.setDesenho(x.getDesenho());
		 d.setCor(x.getCor());
		 d.setTamanhoBordado(x.getTamanho());
		 d.setLocalAplicacao(x.getLocalAplicacao());
		 });case BORDADO_ESCRITA->escritas.findByPersonalizacao_IdPersonalizacao(id).ifPresent(x->{
			 d.setTexto(x.getTexto());
			 d.setFonte(x.getFonte());
			 d.setCor(x.getCor());
			 d.setTamanhoBordado(x.getTamanho());});
		 case APLICACAO_RENDA->rendas.findByPersonalizacao_IdPersonalizacao(id).ifPresent(x->{
			 d.setTipoRenda(x.getTipoRenda());
			 d.setCor(x.getCor());
			 d.setLarguraRenda(x.getLargura());});
		 case TINGIMENTO->tingimentos.findByPersonalizacao_IdPersonalizacao(id).ifPresent(x->d.setCorTingimento(x.getCor()));
		 case ALTERACAO_MANGA->mangas.findByPersonalizacao_IdPersonalizacao(id).ifPresent(x->d.setTipoManga(x.getTipoManga()));
		 } 
	 return d;
	 }
 @Transactional 
 public Personalizacao copiarParaPedido(Personalizacao origem,ItemPedido destino){
	
	 Personalizacao p = new Personalizacao();
	 p.setNivel(origem.getNivel());
	 p.setTipo(origem.getTipo());
	 p.setValor(origem.getValor());
	 p.setItemPedido(destino);

	 p = repo.saveAndFlush(p);
	 
	 
	 PersonalizacaoRequestDTO d=new PersonalizacaoRequestDTO(); 
	 d.setTipo(origem.getTipo());
	 
	 if(origem.getTipo()==TipoAlteracao.BORDADO_DESENHO){
		 var x=desenhos.findByPersonalizacao_IdPersonalizacao(origem.getIdPersonalizacao()).orElseThrow();d.setDesenho(x.getDesenho());d.setCor(x.getCor());
		 d.setTamanhoBordado(x.getTamanho());
		 d.setLocalAplicacao(x.getLocalAplicacao());
		 } 
	 else if(origem.getTipo()==TipoAlteracao.BORDADO_ESCRITA){
		 var x=escritas.findByPersonalizacao_IdPersonalizacao(origem.getIdPersonalizacao()).orElseThrow();
		 d.setTexto(x.getTexto());
		 d.setFonte(x.getFonte());
		 d.setCor(x.getCor());
		 d.setTamanhoBordado(x.getTamanho());
		 } 
	 else if(origem.getTipo()==TipoAlteracao.APLICACAO_RENDA){var x=rendas.findByPersonalizacao_IdPersonalizacao(origem.getIdPersonalizacao()).orElseThrow();d.setTipoRenda(x.getTipoRenda());
	 d.setCor(x.getCor());d.setLarguraRenda(x.getLargura());} else if(origem.getTipo()==TipoAlteracao.TINGIMENTO){d.setCorTingimento(tingimentos.findByPersonalizacao_IdPersonalizacao(origem.getIdPersonalizacao()).orElseThrow().getCor());} else {d.setTipoManga(mangas.findByPersonalizacao_IdPersonalizacao(origem.getIdPersonalizacao()).orElseThrow().getTipoManga());} saveDetail(p,d); destino.getPersonalizacoes().add(p); return p;}
 
 
 
 
 private void excluirDetalhe(Personalizacao p) {
	    Long id = p.getIdPersonalizacao();

	    desenhos.findByPersonalizacao_IdPersonalizacao(id)
	            .ifPresent(desenhos::delete);

	    escritas.findByPersonalizacao_IdPersonalizacao(id)
	            .ifPresent(escritas::delete);

	    rendas.findByPersonalizacao_IdPersonalizacao(id)
	            .ifPresent(rendas::delete);

	    tingimentos.findByPersonalizacao_IdPersonalizacao(id)
	            .ifPresent(tingimentos::delete);

	    mangas.findByPersonalizacao_IdPersonalizacao(id)
	            .ifPresent(mangas::delete);
	}
 
}
