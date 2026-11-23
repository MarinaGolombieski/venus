package com.example.controllers;
import java.util.List; import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*; import com.example.dtos.*; import com.example.services.ProdutoService;
@RestController 
@RequestMapping("/produtos") 
public class ProdutoController {
	private final ProdutoService service;
	public ProdutoController(ProdutoService service){
		this.service=service;
		}
	@GetMapping public List<ProdutoResponseDTO> listar(){
		return service.listarTodos();
		}
	@GetMapping("/{id}") 
	public ResponseEntity<ProdutoResponseDTO> buscar(@PathVariable Long id){
		return ResponseEntity.ok(service.buscarPorId(id));
		}
	
	@GetMapping("/{id}/recomendacoes")
	public ResponseEntity<List<ProdutoResponseDTO>> recomendar(
	        @PathVariable Long id) {

	    return ResponseEntity.ok(service.recomendar(id));
	}
	
	@PostMapping
	public ResponseEntity<ProdutoResponseDTO> criar(@RequestBody ProdutoRequestDTO d){return ResponseEntity.ok(service.salvar(d));}@PutMapping("/{id}") public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id,@RequestBody ProdutoRequestDTO d){return ResponseEntity.ok(service.atualizar(id,d));}@DeleteMapping("/{id}") public ResponseEntity<Void> deletar(@PathVariable Long id){service.deletar(id);return ResponseEntity.noContent().build();}}
