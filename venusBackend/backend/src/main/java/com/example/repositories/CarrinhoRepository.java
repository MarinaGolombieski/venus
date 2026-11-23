package com.example.repositories;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entities.Carrinho;
public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> { Optional<Carrinho> findByUsuario_IdUsuario(Long usuarioId); }
