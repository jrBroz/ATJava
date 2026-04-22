package Infnet.Assessment.service;
import Infnet.Assessment.dto.PainelTaticoDTO.PainelTaticoResponseDTO;
import Infnet.Assessment.model.PainelTaticoMissao;
import Infnet.Assessment.repository.PainelTaticoMissaoRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PainelTaticoMissaoService {

    private PainelTaticoMissaoRepository repository;


    @Cacheable(value = "topMissoesTaticas") 
    @Transactional(readOnly = true)
    public List<PainelTaticoResponseDTO> obterTopMissoesTaticas() {
        
        LocalDateTime dataCorte = LocalDateTime.now().minusDays(15);
        
        List<PainelTaticoMissao> entidades = repository.findTop10ByUltimaAtualizacaoAfterOrderByIndiceProntidaoDesc(dataCorte);
        
        // Convertendo para DTO antes de enviar para o Controller
        return entidades.stream()
                .map(e -> new PainelTaticoResponseDTO(
                    e.getMissaoId(),
                    e.getTitulo(),
                    e.getStatus(),
                    e.getIndiceProntidao()
                ))
                .toList();
    }
}