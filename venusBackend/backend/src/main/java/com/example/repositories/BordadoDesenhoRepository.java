package com.example.repositories;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entities.BordadoDesenho;
public interface BordadoDesenhoRepository extends JpaRepository<BordadoDesenho, Long> {
    Optional<BordadoDesenho> findByPersonalizacao_IdPersonalizacao(Long personalizacaoId);
}
