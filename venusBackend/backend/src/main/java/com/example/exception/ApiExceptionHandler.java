package com.example.exception;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String,Object>> runtime(RuntimeException ex){
        return ResponseEntity.badRequest().body(Map.of("timestamp", LocalDateTime.now().toString(), "status",400,"message",ex.getMessage()==null?"Erro na requisição":ex.getMessage()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> generic(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("timestamp",LocalDateTime.now().toString(),"status",500,"message","Erro interno do servidor"));
    }
}
