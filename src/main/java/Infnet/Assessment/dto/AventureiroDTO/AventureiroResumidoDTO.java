package Infnet.Assessment.dto.AventureiroDTO;
import Infnet.Assessment.enums.ClasseAventureiro;

public record AventureiroResumidoDTO(
 
    ClasseAventureiro classe,
    Integer nivel,
    Boolean ativo
) {} //