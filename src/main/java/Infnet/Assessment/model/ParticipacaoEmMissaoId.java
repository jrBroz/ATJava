package Infnet.Assessment.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class ParticipacaoEmMissaoId implements Serializable{

    private Long missaoId;
    private Long aventureiroId;

}