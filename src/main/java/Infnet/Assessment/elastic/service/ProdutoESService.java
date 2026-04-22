package Infnet.Assessment.elastic.service;
import org.springframework.stereotype.Service;
import Infnet.Assessment.elastic.dto.AgregacaoDTO;
import Infnet.Assessment.elastic.dto.ProdutoDTO.ProdutoResumoDTO;
import Infnet.Assessment.elastic.model.ProdutoES;
import lombok.RequiredArgsConstructor;
import java.util.List;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import java.io.IOException;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // vai criar o construtor para o ElasticsearchClient automaticamente

public class ProdutoESService {
private final ElasticsearchClient client;

    private final String indexName = "produtos";

    // ==========================================
    // PARTE A: BUSCAS TEXTUAIS 
    // ==========================================

    public List<ProdutoResumoDTO> buscarPorNome(String termo) throws IOException {
        SearchResponse<ProdutoES> response = client.search(s -> s
                .index(indexName)
                .query(q -> q.match(m -> m.field("nome").query(termo))), 
                ProdutoES.class);
        return converterParaDto(response);
    }

    // Busca simples na descrição
    public List<ProdutoResumoDTO> buscarPorDescricao(String termo) throws IOException {
        SearchResponse<ProdutoES> response = client.search(s -> s
                .index(indexName)
                .query(q -> q.match(m -> m.field("descricao").query(termo))), 
                ProdutoES.class);
        return converterParaDto(response);
    }

    // Busca por frase exata (Ex: "cura superior" precisa estar nessa ordem)
    public List<ProdutoResumoDTO> buscarFraseExata(String termo) throws IOException {
        SearchResponse<ProdutoES> response = client.search(s -> s
                .index(indexName)
                .query(q -> q.matchPhrase(m -> m.field("descricao").query(termo))), 
                ProdutoES.class);
        return converterParaDto(response);
    }

    // Busca Fuzzy (Tolerante a erros como "espdaa")
    public List<ProdutoResumoDTO> buscarFuzzy(String termo) throws IOException {
        SearchResponse<ProdutoES> response = client.search(s -> s
                .index(indexName)
                .query(q -> q.match(m -> m
                        .field("nome")
                        .query(termo)
                        .fuzziness("AUTO") // A mágica do erro de digitação acontece aqui
                )), 
                ProdutoES.class);
        return converterParaDto(response);
    }

    public List<ProdutoResumoDTO> buscarMultiCampos(String termo) throws IOException {
        SearchResponse<ProdutoES> response = client.search(s -> s
                .index(indexName)
                .query(q -> q.multiMatch(m -> m.fields("nome", "descricao").query(termo))),
                ProdutoES.class);
        return converterParaDto(response);
    }

    // ==========================================
    // PARTE B: FILTROS E BUSCA AVANÇADA
    // ==========================================

    // Busca textual na descrição + Filtro exato por categoria
    public List<ProdutoResumoDTO> buscarComFiltro(String termo, String categoria) throws IOException {
        SearchResponse<ProdutoES> response = client.search(s -> s
                .index(indexName)
                .query(q -> q.bool(b -> b
                        .must(m -> m.match(mt -> mt.field("descricao").query(termo)))
                        .filter(f -> f.term(t -> t.field("categoria").value(categoria)))
                )), 
                ProdutoES.class);
        return converterParaDto(response);
    }

    // Busca apenas por faixa de preço
    public List<ProdutoResumoDTO> buscarPorFaixaPreco(Double min, Double max) throws IOException {
        SearchResponse<ProdutoES> response = client.search(s -> s
                .index(indexName)
                .query(q -> q.range(r -> r.number(n -> n.field("preco").gte(min).lte(max)))), 
                ProdutoES.class);
        return converterParaDto(response);
    }

    public List<ProdutoResumoDTO> buscaAvancada(String categoria, String raridade, Double min, Double max) throws IOException {
        SearchResponse<ProdutoES> response = client.search(s -> s
                .index(indexName)
                .query(q -> q.bool(b -> b
                        .filter(f -> f.term(t -> t.field("categoria").value(categoria)))
                        .filter(f -> f.term(t -> t.field("raridade").value(raridade)))
                        .filter(f -> f.range(r -> r.number(n -> n.field("preco").gte(min).lte(max))))
                )), 
                ProdutoES.class);
        return converterParaDto(response);
    }

    // ==========================================
    // PARTE C: AGREGAÇÕES 
    // ==========================================

    public List<AgregacaoDTO> buscarQuantidadePorCategoria() throws IOException {
        SearchResponse<Void> response = client.search(s -> s
                .index(indexName)
                .size(0)
                .aggregations("contagem_por_categoria", a -> a.terms(t -> t.field("categoria"))), Void.class);

        return response.aggregations().get("contagem_por_categoria").sterms()
                .buckets().array().stream()
                .map(bucket -> new AgregacaoDTO(bucket.key().stringValue(), bucket.docCount()))
                .collect(Collectors.toList());
    }

    public List<AgregacaoDTO> buscarQuantidadePorRaridade() throws IOException {
        SearchResponse<Void> response = client.search(s -> s
                .index(indexName)
                .size(0)
                .aggregations("contagem_por_raridade", a -> a.terms(t -> t.field("raridade"))), Void.class);

        return response.aggregations().get("contagem_por_raridade").sterms()
                .buckets().array().stream()
                .map(bucket -> new AgregacaoDTO(bucket.key().stringValue(), bucket.docCount()))
                .collect(Collectors.toList());
    }

    public Double obterMediaPreco() throws IOException {
        SearchResponse<Void> response = client.search(s -> s
                .index(indexName)
                .size(0)
                .aggregations("media_preco", a -> a.avg(avg -> avg.field("preco"))), Void.class);

        var agg = response.aggregations().get("media_preco").avg();
        return agg != null ? agg.value() : 0.0;
    }

    public List<AgregacaoDTO> obterFaixasPreco() throws IOException {
        SearchResponse<Void> response = client.search(s -> s
                .index(indexName)
                .size(0)
                .aggregations("faixas_de_preco", a -> a
                    .range(r -> r
                        .field("preco")
                        .ranges(rg -> rg.to(100.0))
                        .ranges(rg -> rg.from(100.0).to(300.0))
                        .ranges(rg -> rg.from(300.0).to(700.0))
                        .ranges(rg -> rg.from(700.0))
                    )
                ), Void.class);

        var aggregate = response.aggregations().get("faixas_de_preco");
        if (aggregate == null || !aggregate.isRange()) return List.of();

        return aggregate.range().buckets().array().stream()
                .map(bucket -> new AgregacaoDTO(formatarNomeFaixa(bucket.key()), bucket.docCount()))
                .collect(Collectors.toList());
    }

    // --- metodos auxiliar ---

    private List<ProdutoResumoDTO> converterParaDto(SearchResponse<ProdutoES> response) {
        return response.hits().hits().stream()
                .map(Hit::source)
                .filter(source -> source != null)
                .map(p -> new ProdutoResumoDTO(
                        p.getNome(), p.getDescricao(), p.getPreco(), p.getCategoria(), p.getRaridade()
                ))
                .collect(Collectors.toList());
    }

    private String formatarNomeFaixa(String key) {
        if (key.contains("*-")) return "Abaixo de 100";
        if (key.endsWith("-*")) return "Acima de 700";
        if (key.contains("100.0-300.0")) return "De 100 a 300";
        if (key.contains("300.0-700.0")) return "De 300 a 700";
        return key;
    }
}