package Infnet.Assessment.dto.PainelTaticoDTO;

import java.math.BigDecimal;



public record PainelTaticoResponseDTO(
    Long missaoId,
    String titulo,
    String status,
    BigDecimal indiceProntidao
) {}
