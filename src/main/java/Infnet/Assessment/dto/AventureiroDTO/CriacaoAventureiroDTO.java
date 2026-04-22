package Infnet.Assessment.dto.AventureiroDTO;
import Infnet.Assessment.enums.ClasseAventureiro;

public record CriacaoAventureiroDTO (

     Long organizacaoId,
     Long usuarioId,
     String nome,
     ClasseAventureiro classe,
     Integer nivel
){}