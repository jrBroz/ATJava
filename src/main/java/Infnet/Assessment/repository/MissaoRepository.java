package Infnet.Assessment.repository;
import Infnet.Assessment.enums.NivelPerigoMissao;
import Infnet.Assessment.enums.StatusMissao;
import Infnet.Assessment.model.Missao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface MissaoRepository extends JpaRepository<Missao, Long> {

    // Método para a listagem com filtros, curisaoamente o SpringData consegue monta query  pelo nome
    Page<Missao> findByStatusAndNivelPerigoMissaoAndCreatedAtBetween(
            StatusMissao status, 
            NivelPerigoMissao nivelPerigoMissao, 
            LocalDateTime inicio,
            LocalDateTime fim, 
            Pageable pageable
    );

    // Método para buscar a missão já trazendo os participantes (JOIN FETCH)
    // Isso evita o erro de "LazyInitializationException" quando  ler os aventureiros
@Query("SELECT m FROM Missao m LEFT JOIN FETCH m.participacoes WHERE m.id = :id")
    java.util.Optional<Missao> encontrarComParticipacoes(@Param("id") Long id);
}