package Infnet.Assessment.elastic.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Infnet.Assessment.elastic.dto.AgregacaoDTO;
import Infnet.Assessment.elastic.dto.ProdutoDTO.ProdutoResumoDTO;
import Infnet.Assessment.elastic.service.ProdutoESService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoESController {

    private final ProdutoESService produtoService;

    // --- PARTE A: BUSCAS TEXTUAIS ---

    @GetMapping("/busca/nome")
    public ResponseEntity<List<ProdutoResumoDTO>> buscarPorNome(@RequestParam String termo) throws IOException {
        return ResponseEntity.ok(produtoService.buscarPorNome(termo));
    }

    @GetMapping("/busca/descricao")
    public ResponseEntity<List<ProdutoResumoDTO>> buscarPorDescricao(@RequestParam String termo) throws IOException {
        return ResponseEntity.ok(produtoService.buscarPorDescricao(termo));
    }

    @GetMapping("/busca/frase")
    public ResponseEntity<List<ProdutoResumoDTO>> buscarPorFrase(@RequestParam String termo) throws IOException {
        return ResponseEntity.ok(produtoService.buscarFraseExata(termo));
    }

    @GetMapping("/busca/fuzzy")
    public ResponseEntity<List<ProdutoResumoDTO>> buscarFuzzy(@RequestParam String termo) throws IOException {
        return ResponseEntity.ok(produtoService.buscarFuzzy(termo));
    }

    @GetMapping("/busca/multicampos")
    public ResponseEntity<List<ProdutoResumoDTO>> buscarMultiCampos(@RequestParam String termo) throws IOException {
        return ResponseEntity.ok(produtoService.buscarMultiCampos(termo));
    }

    // --- PARTE B: BUSCAS COM FILTROS ---

    @GetMapping("/busca/com-filtro")
    public ResponseEntity<List<ProdutoResumoDTO>> buscarComFiltro(
            @RequestParam String termo, 
            @RequestParam String categoria) throws IOException {
        return ResponseEntity.ok(produtoService.buscarComFiltro(termo, categoria));
    }

    @GetMapping("/busca/faixa-preco")
    public ResponseEntity<List<ProdutoResumoDTO>> buscarPorFaixaPreco(
            @RequestParam Double min, 
            @RequestParam Double max) throws IOException {
        return ResponseEntity.ok(produtoService.buscarPorFaixaPreco(min, max));
    }

    @GetMapping("/busca/avancada")
    public ResponseEntity<List<ProdutoResumoDTO>> buscarAvancada(
            @RequestParam String categoria,
            @RequestParam String raridade,
            @RequestParam Double min,
            @RequestParam Double max) throws IOException {
        return ResponseEntity.ok(produtoService.buscaAvancada(categoria, raridade, min, max));
    }

    // --- PARTE C: AGREGAÇÕES ---

    @GetMapping("/agregacoes/por-categoria")
    public ResponseEntity<List<AgregacaoDTO>> buscarQuantidadePorCategoria() throws IOException {
        return ResponseEntity.ok(produtoService.buscarQuantidadePorCategoria());
    }

    @GetMapping("/agregacoes/por-raridade")
    public ResponseEntity<List<AgregacaoDTO>> buscarQuantidadePorRaridade() throws IOException {
        return ResponseEntity.ok(produtoService.buscarQuantidadePorRaridade());
    }

    @GetMapping("/agregacoes/preco-medio")
    public ResponseEntity<Double> obterMediaPreco() throws IOException {
        return ResponseEntity.ok(produtoService.obterMediaPreco());
    }

    @GetMapping("/agregacoes/faixas-preco")
    public ResponseEntity<List<AgregacaoDTO>> obterFaixasPreco() throws IOException {
        return ResponseEntity.ok(produtoService.obterFaixasPreco());
    }
}