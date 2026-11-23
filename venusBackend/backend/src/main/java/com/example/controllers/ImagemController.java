package com.example.controllers;
import java.util.List; 
import org.springframework.http.*; 
import org.springframework.web.bind.annotation.*; 
import com.example.dtos.*; 
import com.example.services.ImagemService;
@RestController 
@RequestMapping("/imagens") 
public class ImagemController {
	private final ImagemService service;
	public ImagemController(ImagemService service){
		this.service=service;
		}
	@GetMapping 
	public List<ImagemResponseDTO> listar(){
		return service.listarTodos();}
	@GetMapping("/{id}") 
	public ResponseEntity<byte[]> arquivo(@PathVariable Long id){
		var i=service.buscar(id);
		MediaType type = MediaType.APPLICATION_OCTET_STREAM;
		try { if (i.getContentType()!=null) type = MediaType.parseMediaType(i.getContentType()); } catch (Exception ignored) {}
		return ResponseEntity.ok().contentType(type).body(i.getArquivo());
		}
	@PostMapping("/upload") 
	public ResponseEntity<ImagemResponseDTO> upload(@RequestParam Long produtoId,@RequestParam org.springframework.web.multipart.MultipartFile arquivo){
		return ResponseEntity.ok(service.salvarMultipart(produtoId,arquivo));
		}
@PostMapping 
public ResponseEntity<ImagemResponseDTO> criar(@RequestBody ImagemRequestDTO d){
	return ResponseEntity.ok(service.salvar(d));
	}

@DeleteMapping("/{id}") 
public ResponseEntity<Void> deletar(@PathVariable Long id){
	service.deletar(id);
	return ResponseEntity.noContent().build();
	}
}
