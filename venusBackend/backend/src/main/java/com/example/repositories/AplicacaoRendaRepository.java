package com.example.repositories;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entities.AplicacaoRenda;
public interface AplicacaoRendaRepository extends JpaRepository<AplicacaoRenda, Long> {
    Optional<AplicacaoRenda> findByPersonalizacao_IdPersonalizacao(Long personalizacaoId);
}
