package Infnet.Assessment.elastic.dto.ProdutoDTO;

public record ProdutoResumoDTO(
    String nome,
    String descricao,
    Double preco,
    String categoria,
    String raridade
) {}