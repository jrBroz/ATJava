package Infnet.Assessment.dto.ParticipanteDTO;

import java.math.BigDecimal;

public record ParticipanteResumoDTO(

    String nomeAventureiro,
    String papel,           // Papel desempenhado
    BigDecimal recompensa,      // Valor recebido
    boolean destaqueMvp    // Indicação de destaque
) {}