package com.example.repositories;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entities.AlteracaoManga;
public interface AlteracaoMangaRepository extends JpaRepository<AlteracaoManga, Long> {
    Optional<AlteracaoManga> findByPersonalizacao_IdPersonalizacao(Long personalizacaoId);
}
