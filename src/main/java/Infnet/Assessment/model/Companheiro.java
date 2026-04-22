package Infnet.Assessment.model;
import Infnet.Assessment.enums.EspecieCompanheiro;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "companheiro", schema = "operacoes")
public class Companheiro {

    @Id
    @Column(name = "aventureiro_id")
    private Long aventureiroId;

    @Column(nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EspecieCompanheiro especie;

    @Column(nullable = false)

    private Integer indice_lealdade;
}