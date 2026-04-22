package Infnet.Assessment.repository;

import Infnet.Assessment.model.PainelTaticoMissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PainelTaticoMissaoRepository extends JpaRepository<PainelTaticoMissao, Long> {
    
    // já q o spring monta query baseado em nome de funcao, deixei assim, bem mais rapido****** :
    // SELECT * FROM operacoes.mv_painel_tatico_missao WHERE ultima_atualizacao > ? ORDER BY indice_prontidao DESC LIMIT 10 (S)
    List<PainelTaticoMissao> findTop10ByUltimaAtualizacaoAfterOrderByIndiceProntidaoDesc(LocalDateTime dataCorte);
}