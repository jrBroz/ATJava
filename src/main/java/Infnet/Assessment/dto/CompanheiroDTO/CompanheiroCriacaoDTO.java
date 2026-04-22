package Infnet.Assessment.dto.CompanheiroDTO;
import Infnet.Assessment.enums.EspecieCompanheiro;


public record CompanheiroCriacaoDTO(

    String nome,
    EspecieCompanheiro especie,    
    Integer lealdade
) {} 

