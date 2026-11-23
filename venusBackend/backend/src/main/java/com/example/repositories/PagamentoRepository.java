package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

}
