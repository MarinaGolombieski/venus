package com.example.controllers;

import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.dtos.ProdutoResponseDTO;
import com.example.entities.Produto;
import com.example.repositories.ProdutoRepository;
import com.example.utils.DtoMapper;

@RestController
@RequestMapping("/recomendacoes")
public class RecomendacaoController {
    private final ProdutoRepository produtos;
    public RecomendacaoController(ProdutoRepository produtos){this.produtos=produtos;}


@GetMapping("/produto/{id}")
public ResponseEntity<List<ProdutoResponseDTO>> porProduto(
        @PathVariable Long id) {

    Produto base = produtos.findById(id)
        .orElseThrow(() ->
            new RuntimeException("Produto não encontrado"));

    List<ProdutoResponseDTO> result = produtos.findAll().stream()
        .filter(p -> !Objects.equals(p.getId(), base.getId()))
        .filter(p -> score(base, p) >= 4)
        .sorted(
            Comparator.comparingInt(
                (Produto p) -> score(base, p)
            ).reversed()
        )
        .limit(6)
        .map(DtoMapper::produto)
        .toList();

    return ResponseEntity.ok(result);
}

    private int score(Produto a, Produto b){
        int s=0;
        if(a.getTipo()==b.getTipo()) s+=3;
        if(a.getEstado()==b.getEstado()) s+=2;
        if(a.getCategoria()!=null && b.getCategoria()!=null && Objects.equals(a.getCategoria().getIdCategoria(),b.getCategoria().getIdCategoria())) s+=4;
        if(a.getTamanho()==b.getTamanho()) s+=1;
        return s;
    }
}
