package Infnet.Assessment.dto.MissaoDTO;
import java.time.LocalDateTime;


public record MissaoDetalhadaDTO (

     Long id,
     String titulo,
     String status,
     String nivelPerigo,
     LocalDateTime createdAt

){}
