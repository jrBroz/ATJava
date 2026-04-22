package Infnet.Assessment.controller;
import Infnet.Assessment.dto.AventureiroDTO.AventureiroResponseDTO;
import Infnet.Assessment.dto.AventureiroDTO.AventureiroResumidoDTO;
import Infnet.Assessment.dto.AventureiroDTO.CriacaoAventureiroDTO;
import Infnet.Assessment.dto.RankingDTO.RankingDTO;
import Infnet.Assessment.dto.CompanheiroDTO.CompanheiroCriacaoDTO;
import Infnet.Assessment.service.AventureiroService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/aventureiros")
public class AventureiroController {

    private final AventureiroService service;

    public AventureiroController(AventureiroService AventureiroService) {
        this.service = AventureiroService;
    }

    @GetMapping
    public ResponseEntity<List<AventureiroResumidoDTO>> getAll(
            AventureiroResumidoDTO filtro,
            @RequestHeader(value = "X-Page", defaultValue = "0")
            @Min(value = 0, message = "O tamanho deve ser maior que -1") int page,
            @RequestHeader(value = "X-Size", defaultValue = "10")
            @Min(value = 1, message = "O tamanho deve ser maior que 0")
            @Max(value = 50, message = "O tamanho deve ser menor que 51") int size)
    {

        Page<AventureiroResumidoDTO> pagina = service.getAll(filtro, page, size);

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(pagina.getTotalElements()))
                .header("X-Total-Pages", String.valueOf(pagina.getTotalPages()))
                .header("X-Page", String.valueOf(pagina.getNumber()))
                .header("X-Size", String.valueOf(pagina.getNumberOfElements()))
                .body(pagina.getContent());
    }

    
    // dava pra colocar no insomnia, mas é masi pratico e pelo menos ganho um pouco de tempo em deixar aq no codigo msm e o retorno no insomnia é + limpo
    @GetMapping("/filtros")
    public ResponseEntity<List<AventureiroResumidoDTO>> filtrar(
            @RequestParam String nome,
            @RequestHeader(value = "X-Page", defaultValue = "0")
            @Min(value = 0, message = "O tamanho deve ser maior que -1") int page,
            @RequestHeader(value = "X-Size", defaultValue = "10")
            @Min(value = 1, message = "O tamanho deve ser maior que 0")
            @Max(value = 50, message = "O tamanho deve ser menor que 51") int size)
    {
        Page<AventureiroResumidoDTO> aventureiros = service.filtrarPorNome(nome, page, size);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(aventureiros.getTotalElements()))
                .header("X-Total-Pages", String.valueOf(aventureiros.getTotalPages()))
                .header("X-Page", String.valueOf(aventureiros.getNumber()))
                .header("X-Size", String.valueOf(aventureiros.getNumberOfElements()))
                .body(aventureiros.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AventureiroResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/ranking")
    public ResponseEntity<Page<RankingDTO>> obterRanking(
    
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(service.gerarRanking(pageable));
    }

    @PostMapping //criar aventureiro
    public ResponseEntity<AventureiroResponseDTO> create(@Valid @RequestBody CriacaoAventureiroDTO aventureiroCriacao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(aventureiroCriacao));
    }

    @PatchMapping("/{id}/inativar") //desligar da guilda
    public ResponseEntity<AventureiroResponseDTO> inativar(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(service.inativar(id));
    }

    @PatchMapping("/{id}/recrutar") //recruta p guiild
    public ResponseEntity<AventureiroResponseDTO> recrutar(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(service.recrutar(id));
    }

    @PutMapping("/{id}") //alterar aventureiro
    public ResponseEntity<AventureiroResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CriacaoAventureiroDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    // Bota um companheiro no id do aventureiro
    @PutMapping("/{id}/atribuirCompanheiro")
    public ResponseEntity<AventureiroResponseDTO> atribuirCompanheiro(@PathVariable Long id, @Valid @RequestBody CompanheiroCriacaoDTO companheiro) {
        return ResponseEntity.ok(service.atribuirCompanheiro(id, companheiro));
    }

    @DeleteMapping("/{id}/removerCompanheiro")
    public ResponseEntity<AventureiroResponseDTO> removerCompanheiro(@PathVariable Long id) {
        return ResponseEntity.ok(service.removerCompanheiro(id));
    }
}