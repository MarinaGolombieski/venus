package com.example.controllers;
import java.util.List; 
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*; 
import com.example.dtos.*; 
import com.example.services.CarrinhoService;

@RestController 
@RequestMapping("/carrinhos") 
public class CarrinhoController {
	private final CarrinhoService service;
	
	public CarrinhoController(CarrinhoService service){
		this.service=service;
		}
	@GetMapping 
	public List<CarrinhoResponseDTO> listar(){
		return service.listarTodos();
		}
	@GetMapping("/usuario/{usuarioId}") public ResponseEntity<CarrinhoResponseDTO> buscarPorUsuario(@PathVariable Long usuarioId){
		return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
		}
@GetMapping("/{id}") 
public ResponseEntity<CarrinhoResponseDTO> buscar(@PathVariable Long id){
	return ResponseEntity.ok(service.buscar(id));
	}
@PostMapping 
public ResponseEntity<CarrinhoResponseDTO> criar(@RequestBody CarrinhoRequestDTO d){
	return ResponseEntity.ok(service.criar(d));
	}
@PostMapping("/{id}/itens") 
public ResponseEntity<ItemCarrinhoResponseDTO> adicionar(@PathVariable Long id,@RequestBody ItemCarrinhoRequestDTO d){
	d.setCarrinhoId(id);
	return ResponseEntity.ok(service.adicionarItem(d));
	}
@PutMapping("/itens/{itemId}")
public ResponseEntity<ItemCarrinhoResponseDTO> quantidade(@PathVariable Long itemId,@RequestParam int quantidade){
	return ResponseEntity.ok(service.atualizarItem(itemId,quantidade));
	}
@DeleteMapping("/itens/{itemId}") 
public ResponseEntity<Void> remover(@PathVariable Long itemId){
	service.removerItem(itemId);
	return ResponseEntity.noContent().build();
	}
@DeleteMapping("/{id}") 
public ResponseEntity<Void> deletar(@PathVariable Long id){
	throw new RuntimeException("Carrinho não deve ser excluído pela API de compra");
}
}
