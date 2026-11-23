package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.ItemCarrinho;

public interface ItemCarrinhoRepository extends JpaRepository<ItemCarrinho, Long> {

}