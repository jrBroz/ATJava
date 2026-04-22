package Infnet.Assessment.controller;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import Infnet.Assessment.dto.MissaoDTO.MissaoDetalhadaDTO;
import Infnet.Assessment.dto.PainelTaticoDTO.PainelTaticoResponseDTO;
import Infnet.Assessment.enums.NivelPerigoMissao;
import Infnet.Assessment.enums.StatusMissao;
import Infnet.Assessment.model.Missao;
import Infnet.Assessment.service.MissaoService;
import Infnet.Assessment.service.PainelTaticoMissaoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/missoes")
@RequiredArgsConstructor 
public class MissaoController {

  private final MissaoService service;
  private final PainelTaticoMissaoService painelTaticoMissaoService;

    // Endpoint: Listagem com filtros, paginação e ordenação
@GetMapping("/busca-avancada")
public ResponseEntity<Page<Missao>> listar(
        @RequestParam(required = false) StatusMissao status,
        @RequestParam(required = false) NivelPerigoMissao nivel,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
        Pageable pageable) {
    return ResponseEntity.ok(service.listarComFiltros(status, nivel, inicio, fim, pageable));
}

 @GetMapping("/relatorios/ranking")
public ResponseEntity<List<Object[]>> ranking(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
        @RequestParam(required = false) StatusMissao status) {
    
    // Se não mandarem data, define um padrão (ex: os últimos 30 dias)

    if (inicio == null) inicio = LocalDateTime.now().minusDays(30);
    if (fim == null) fim = LocalDateTime.now();

    return ResponseEntity.ok(service.gerarRelatorioMetricas(inicio, fim));
}

// Endpoint: Detalhamento de uma missão específica
    @GetMapping("/{id}")
    public ResponseEntity<MissaoDetalhadaDTO> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(service.obterDetalhes(id));
    }


@GetMapping("/relatorios/metricas")
public ResponseEntity<?> buscarMetricas(
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
) { 
    // Se 'inicio' for nulo, pega de 30 dias atrás
    if (inicio == null) {
        inicio = LocalDateTime.now().minusDays(30);
    }
    
    if (fim == null) {
        fim = LocalDateTime.now();
    }

    var metricas = service.gerarRelatorioMetricas(inicio, fim);
    return ResponseEntity.ok(metricas);
}


    @GetMapping("/top15dias")
    public ResponseEntity<List<PainelTaticoResponseDTO>> listarTopMissoesTaticas() {
        List<PainelTaticoResponseDTO> topMissoes = painelTaticoMissaoService.obterTopMissoesTaticas();
        return ResponseEntity.ok(topMissoes);
    }
}