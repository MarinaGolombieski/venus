package com.example.controllers;
import java.util.List; 
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*; 
import com.example.dtos.*; 
import com.example.services.CategoriaService;

@RestController 
@RequestMapping("/categorias") 
public class CategoriaController {
	private final CategoriaService service;
	
	public CategoriaController(CategoriaService service){
		this.service=service;
		}
	@GetMapping 
	public List<CategoriaResponseDTO> listar(){
		return service.listarTodos();
		}
	@GetMapping("/{id}") 
	public ResponseEntity<CategoriaResponseDTO> buscar(@PathVariable Long id){
		return ResponseEntity.ok(service.buscar(id));
		}
	@PostMapping 
	public ResponseEntity<CategoriaResponseDTO> criar(@RequestBody CategoriaRequestDTO d){
		return ResponseEntity.ok(service.salvar(d));
		}
	@PutMapping("/{id}") 
	public ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable Long id,@RequestBody CategoriaRequestDTO d){
		return ResponseEntity.ok(service.atualizar(id,d));
		}
	@DeleteMapping("/{id}") 
	public ResponseEntity<Void> deletar(@PathVariable Long id){
		service.deletar(id);
		return ResponseEntity.noContent().build();
		}
	}
