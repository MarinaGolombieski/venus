package com.example.controllers;
import java.util.List; 
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*; 
import com.example.dtos.*; 
import com.example.services.MaterialService;
@RestController 
@RequestMapping("/materiais") 
public class MaterialController {
	private final MaterialService service;
	public MaterialController(MaterialService service){
		this.service=service;
		}
	@GetMapping public List<MaterialResponseDTO> listar(){
		return service.listarTodos();
		}
	@GetMapping("/{id}") 
	public ResponseEntity<MaterialResponseDTO> buscar(@PathVariable Long id){
		return ResponseEntity.ok(service.buscar(id));
		}
	@PostMapping 
	public ResponseEntity<MaterialResponseDTO> criar(@RequestBody MaterialRequestDTO d){
		return ResponseEntity.ok(service.salvar(d));
		}
	@PutMapping("/{id}") 
	public ResponseEntity<MaterialResponseDTO> atualizar(@PathVariable Long id,@RequestBody MaterialRequestDTO d){
		return ResponseEntity.ok(service.atualizar(id,d));
		}
	@DeleteMapping("/{id}") 
	public ResponseEntity<Void> deletar(@PathVariable Long id){
		service.deletar(id);
		return ResponseEntity.noContent().build();
		}
	}
