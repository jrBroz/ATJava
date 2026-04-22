package Infnet.Assessment.model;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import java.util.List;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "organizacoes", schema = "audit")
public class Organizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    // Ver aq depois

    @OneToMany(mappedBy = "organizacao")
    private List<Aventureiro> aventureiros;

    @OneToMany(mappedBy = "organizacao")
    private List<Missao> missoes;
}
