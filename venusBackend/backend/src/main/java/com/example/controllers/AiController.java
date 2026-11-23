package com.example.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.dtos.*;

@RestController
@RequestMapping("/ia")
public class AiController {
    @PostMapping("/preview")
    public ResponseEntity<AiPreviewResponseDTO> preview(@RequestBody AiPreviewRequestDTO request){
        // Contrato pronto para conectar o provedor de geração de imagem.
        // A chave/API externa deve ficar no Spring Boot, nunca no React.
        return ResponseEntity.status(501).body(new AiPreviewResponseDTO(
            "NOT_CONFIGURED",
            "A prévia por IA está com o contrato pronto, mas o provedor de geração ainda não foi configurado.",
            null
        ));
    }
}
