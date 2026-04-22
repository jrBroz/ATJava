package Infnet.Assessment.model;
import java.time.LocalDateTime;
import Infnet.Assessment.enums.NivelPerigoMissao;
import Infnet.Assessment.enums.StatusMissao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "missao", schema = "operacoes")
public class Missao{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(name="nivel_perigo",nullable = false)
    private NivelPerigoMissao nivelPerigoMissao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMissao status;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    @OneToMany(mappedBy = "missao", fetch = FetchType.LAZY)
    private java.util.List<ParticipacaoEmMissao> participacoes;


    @PrePersist
    protected void onCreate() {
        // Registro automático da data de criação
        this.createdAt = LocalDateTime.now();
        
        //td missao vai comecar com planejada
        if (this.status == null) {
            this.status = StatusMissao.PLANEJADA;
        }
    }

    @PreUpdate
    protected void onUpdate() {

        if ((this.status == StatusMissao.CONCLUIDA || this.status == StatusMissao.CANCELADA) 
            && this.dataFim == null) {
            this.dataFim = LocalDateTime.now();
        }
    }
}