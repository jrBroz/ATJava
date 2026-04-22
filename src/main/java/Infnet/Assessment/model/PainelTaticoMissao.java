package Infnet.Assessment.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Immutable // Proteção contra o Boss (DBA): Garante que o JPA nunca tentará atualizar essa view
@Table(name = "vw_painel_tatico_missao", schema = "operacoes")
public class PainelTaticoMissao {

    @Id
    @Column(name = "missao_id")
    private Long missaoId;

    private String titulo;
    
    // Tratando como String para evitar erro de parse caso o Enum não bata com a View
    private String status; 

    @Column(name = "nivel_perigo")
    private String nivelPerigo;
    
    @Column(name = "organizacao_id")
    private Long organizacaoId;

    @Column(name = "total_participantes")
    private Integer totalParticipantes;
    
    @Column(name = "nivel_medio_equipe")
    private BigDecimal nivelMedioEquipe;
    
    @Column(name = "total_recompensa")
    private BigDecimal totalRecompensa;
    
    @Column(name = "total_mvps")
    private Integer totalMvps;    
    
    @Column(name = "participantes_com_companheiro")
    private Integer participantesComCompanheiro;
    
    @Column(name = "ultima_atualizacao")
    private LocalDateTime ultimaAtualizacao;
    
    @Column(name = "indice_prontidao")
    private BigDecimal indiceProntidao;
}
