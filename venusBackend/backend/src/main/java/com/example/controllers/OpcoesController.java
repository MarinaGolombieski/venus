package com.example.controllers;

import java.math.BigDecimal;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import Enum.*;

@RestController
@RequestMapping("/opcoes")
public class OpcoesController {
    @GetMapping("/personalizacoes")
    public Map<String,Object> personalizacoes() {
        Map<String,Object> r = new LinkedHashMap<>();
        Map<String,String> precos = new LinkedHashMap<>();
        precos.put("BORDADO_DESENHO", "5.00");
        precos.put("BORDADO_ESCRITA", "5.00");
        precos.put("TINGIMENTO", "10.00");
        precos.put("APLICACAO_RENDA", "12.00");
        precos.put("ALTERACAO_MANGA", "18.00");
        r.put("maximoPorItem", 5);
        r.put("precos", precos);
        r.put("tipos", TipoAlteracao.values());
        r.put("niveis", NivelPersonalizacao.values());
        r.put("desenhos", Desenho.values());
        r.put("coresBordadoERenda", Cor.values());
        r.put("coresTingimento", CorTingimento.values());
        r.put("tamanhosBordado", TamanhoBordado.values());
        r.put("locaisAplicacao", LocalAplicacao.values());
        r.put("fontes", Fonte.values());
        r.put("tiposRenda", TipoRenda.values());
        r.put("largurasRenda", LarguraRenda.values());
        r.put("tiposManga", TipoManga.values());
        r.put("metodosPagamento", MetodoPagamento.values());
        return r;
    }
}
