package Infnet.Assessment.dto.ParticipacaoDTO;
import java.math.BigDecimal;
import Infnet.Assessment.enums.PapelAventureiroMissao;


public record ParticipacaoRequestDTO(
    Long missaoId,
    Long aventureiroId,
    PapelAventureiroMissao papel,
    BigDecimal recompensaOuro,
    Boolean mvp
) {}