package com.example.repositories;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entities.Tingimento;
public interface TingimentoRepository extends JpaRepository<Tingimento, Long> {
    Optional<Tingimento> findByPersonalizacao_IdPersonalizacao(Long personalizacaoId);
}
