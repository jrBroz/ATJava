package Infnet.Assessment.dto.RankingDTO;
import java.math.BigDecimal;


public record RankingDTO (
 
    String nome,
    Long totalParticipacoe,
    BigDecimal somaRecompensas,
    Long quantidadeDestaques
){}