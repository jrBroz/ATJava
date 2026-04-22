package Infnet.Assessment.repository;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import Infnet.Assessment.dto.RankingDTO.RankingDTO;
import Infnet.Assessment.model.Aventureiro;

@Repository
public interface AventureiroRepository extends JpaRepository<Aventureiro, Long> {
Page<Aventureiro> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Page<Aventureiro> findByAtivoAndClasseAndNivelGreaterThanEqual(
        Boolean ativo, String classe, Integer nivelMinimo, Pageable pageable);

  @Query("""
        SELECT new Infnet.Assessment.dto.RankingDTO.RankingDTO(
            p.aventureiro.nome, 
            COUNT(p), 
            SUM(p.recompensaOuro), 
            SUM(CASE WHEN p.mvp = true THEN 1L ELSE 0L END)
        ) 
        FROM ParticipacaoEmMissao p 
        WHERE (:inicio IS NULL OR p.missao.createdAt >= :inicio)
        AND (:fim IS NULL OR p.missao.createdAt <= :fim)
        GROUP BY p.aventureiro.id, p.aventureiro.nome 
        ORDER BY SUM(p.recompensaOuro) DESC
    """)
    Page<RankingDTO> buscarRankingParaDTO(
        @Param("inicio") LocalDateTime inicio, 
        @Param("fim") LocalDateTime fim, 
        Pageable pageable
    );
}