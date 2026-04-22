package Infnet.Assessment.elastic.model;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Document(indexName = "guilda_loja")
public class ProdutoES {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String nome;

    @Field(type = FieldType.Text)
    private String descricao;

    @Field(type = FieldType.Keyword)
    private String categoria;

    @Field(type = FieldType.Keyword)
    private String raridade;

    @Field(type = FieldType.Double)
    private Double preco;
}