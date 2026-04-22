package Infnet.Assessment.dto.CompanheiroDTO;
import Infnet.Assessment.enums.EspecieCompanheiro;

public record CompanheiroRequestDTO 
(
     String nome,
     EspecieCompanheiro especieCompanheiro,
     Integer nivelLealdade
){}