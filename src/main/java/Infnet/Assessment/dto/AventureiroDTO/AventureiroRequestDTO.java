package Infnet.Assessment.dto.AventureiroDTO;
import Infnet.Assessment.dto.CompanheiroDTO.CompanheiroRequestDTO;
import Infnet.Assessment.enums.ClasseAventureiro;


public record AventureiroRequestDTO (

    String nomeAventureiro,
    ClasseAventureiro classeAventureiro,
    Integer nivelAventureiro,
    CompanheiroRequestDTO  companheiroRequestDTO,
    Long organizacaoId,
    Long usuarioId    

){}