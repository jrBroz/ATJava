package Infnet.Assessment.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import Infnet.Assessment.enums.PapelAventureiroMissao;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "participacao_missao", schema = "operacoes")
public class ParticipacaoEmMissao {


    // Parte dos ID

    @EmbeddedId
    private ParticipacaoEmMissaoId missaoId = new ParticipacaoEmMissaoId(); // Vai "conversar" com o @MapsId("missaoId") 


    @ManyToOne
    @MapsId("missaoId") 
    @JoinColumn(name = "missao_id", nullable = false)
    private Missao missao;

    @ManyToOne
    @MapsId("aventureiroId") 
    @JoinColumn(name = "aventureiro_id")
    private Aventureiro aventureiro;

    // Resto dos atributo

    @Enumerated(EnumType.STRING)
    @Column(name = "papel", nullable = false) // checar se realmente tá papel e n role
    private PapelAventureiroMissao papel; 

    @Column(name = "recompensa_ouro")
    private BigDecimal recompensaOuro;

    @Column(name = "destaque", nullable = false)
    private Boolean mvp;

    @Column(name = "data_registro", nullable = false, updatable = false)
    private LocalDateTime dataRegistro;

    @PrePersist
    protected void onCreate() {
        this.dataRegistro = LocalDateTime.now();
    }
}