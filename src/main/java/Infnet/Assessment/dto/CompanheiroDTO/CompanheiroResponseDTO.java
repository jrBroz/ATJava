package Infnet.Assessment.dto.CompanheiroDTO;
import Infnet.Assessment.enums.EspecieCompanheiro;

public record CompanheiroResponseDTO (

    Long id,               
    String nomeCompanheiro,           
    EspecieCompanheiro especieCompanheiro  
){}