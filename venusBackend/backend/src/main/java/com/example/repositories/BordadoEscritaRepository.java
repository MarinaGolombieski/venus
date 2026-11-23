package com.example.repositories;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entities.BordadoEscrita;
public interface BordadoEscritaRepository extends JpaRepository<BordadoEscrita, Long> {
    Optional<BordadoEscrita> findByPersonalizacao_IdPersonalizacao(Long personalizacaoId);
}
