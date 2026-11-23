package com.example.services;
import java.util.List; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import Enum.*; import com.example.dtos.*; import com.example.entities.*; import com.example.repositories.*; import com.example.utils.DtoMapper;
@Service
public class PagamentoService {
 private final PagamentoRepository repo; private final PedidoRepository pedidos; private final PedidoService pedidoService;
 public PagamentoService(PagamentoRepository repo,PedidoRepository pedidos,PedidoService pedidoService){this.repo=repo;this.pedidos=pedidos;this.pedidoService=pedidoService;}
 @Transactional(readOnly=true) public List<PagamentoResponseDTO> listarTodos(){return repo.findAll().stream().map(DtoMapper::pagamento).toList();}
 public Pagamento entity(Long id){return repo.findById(id).orElseThrow(()->new RuntimeException("Pagamento não encontrado"));}
 public PagamentoResponseDTO buscar(Long id){return DtoMapper.pagamento(entity(id));}
 @Transactional public PagamentoResponseDTO criar(PagamentoRequestDTO d){if(d==null||d.getPedidoId()==null||d.getMetodoPagamento()==null)throw new RuntimeException("pedidoId e metodoPagamento são obrigatórios");Pedido p=pedidos.findById(d.getPedidoId()).orElseThrow(()->new RuntimeException("Pedido não encontrado"));if(p.getStatus()!=StatusPedido.PENDENTE)throw new RuntimeException("O pedido não está disponível para pagamento");if(p.getPagamento()!=null)throw new RuntimeException("O pedido já possui pagamento");Pagamento pg=new Pagamento();pg.setPedido(p);pg.setValor(p.getValorTotal());pg.setMetodoPagamento(d.getMetodoPagamento());pg.setStatus(StatusPagamento.PENDENTE);Pagamento salvo=repo.save(pg);p.setPagamento(salvo);pedidos.save(p);return DtoMapper.pagamento(salvo);}
 @Transactional public PagamentoResponseDTO aprovar(Long id){Pagamento p=entity(id);if(p.getStatus()!=StatusPagamento.PENDENTE)throw new RuntimeException("Pagamento não está pendente");p.setStatus(StatusPagamento.APROVADO);p.getPedido().setStatus(StatusPedido.PAGO);return DtoMapper.pagamento(repo.save(p));}
 @Transactional public PagamentoResponseDTO recusar(Long id){Pagamento p=entity(id);if(p.getStatus()!=StatusPagamento.PENDENTE)throw new RuntimeException("Pagamento não está pendente");p.setStatus(StatusPagamento.RECUSADO); if(p.getPedido()!=null)pedidoService.cancelarPorPagamento(p.getPedido()); return DtoMapper.pagamento(repo.save(p));}
 @Transactional public PagamentoResponseDTO cancelar(Long id){Pagamento p=entity(id);p.setStatus(StatusPagamento.CANCELADO);if(p.getPedido()!=null)pedidoService.cancelarPorPagamento(p.getPedido());return DtoMapper.pagamento(repo.save(p));}
}
