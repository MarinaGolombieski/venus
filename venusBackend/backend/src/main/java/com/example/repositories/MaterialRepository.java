package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.Material;

public interface MaterialRepository extends JpaRepository<Material, Long> {

}
