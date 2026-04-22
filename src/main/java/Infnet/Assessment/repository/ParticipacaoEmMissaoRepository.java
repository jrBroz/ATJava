package Infnet.Assessment.repository;

import Infnet.Assessment.model.ParticipacaoEmMissao;
import Infnet.Assessment.enums.StatusMissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipacaoEmMissaoRepository extends JpaRepository<ParticipacaoEmMissao, Long> {

    Optional<ParticipacaoEmMissao> findByMissao_IdAndAventureiro_Id(Long missaoId, Long aventureiroId);

    @Query("SELECT p FROM ParticipacaoEmMissao p WHERE p.aventureiro.id = :id ORDER BY p.dataRegistro DESC LIMIT 1")
    Optional<ParticipacaoEmMissao> findUltimaParticipacao(@Param("id") Long id);

    List<ParticipacaoEmMissao> findByMissao_Id(Long missaoId);

    Long countByAventureiro_Id(Long id);

    Optional<ParticipacaoEmMissao> findFirstByAventureiro_IdOrderByMissao_CreatedAtDesc(Long idAv);

@Query("""
        SELECT p.aventureiro.nome, 
               COUNT(p), 
               SUM(p.recompensaOuro), 
               SUM(CASE WHEN p.mvp = true THEN 1L ELSE 0L END) 
        FROM ParticipacaoEmMissao p 
        WHERE p.dataRegistro BETWEEN :inicio AND :fim 
        AND (:status IS NULL OR p.missao.status = :status) 
        GROUP BY p.aventureiro.id, p.aventureiro.nome 
        ORDER BY COUNT(p) DESC
    """)
    List<Object[]> buscarRanking(
        @Param("inicio") LocalDateTime inicio, 
        @Param("fim") LocalDateTime fim, 
        @Param("status") StatusMissao status
    );
    @Query("""
        SELECT p.missao.titulo, 
               p.missao.status, 
               p.missao.nivelPerigoMissao, 
               COUNT(p.aventureiro.id), 
               SUM(p.recompensaOuro)
        FROM ParticipacaoEmMissao p
        WHERE p.missao.createdAt BETWEEN :inicio AND :fim
        GROUP BY p.missao.id, p.missao.titulo, p.missao.status, p.missao.nivelPerigoMissao
    """)
    List<Object[]> buscarMetricasMissoes(@Param("inicio") LocalDateTime inicio, 
                                         @Param("fim") LocalDateTime fim);
}