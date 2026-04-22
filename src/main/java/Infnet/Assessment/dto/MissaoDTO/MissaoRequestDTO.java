package Infnet.Assessment.dto.MissaoDTO;
import Infnet.Assessment.enums.NivelPerigoMissao;
import Infnet.Assessment.enums.StatusMissao;

import java.time.LocalDateTime;

public record MissaoRequestDTO(

    Long organizacaoId,
    String titulo,
    NivelPerigoMissao nivelPerigo,
    StatusMissao status,
    LocalDateTime dataInicio,
    LocalDateTime dataTermino
) {}