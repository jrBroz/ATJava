package TP1.Gloria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdventurerRankingDTO {

    private long adventurerID;
    private String adventurerName;
    private BigDecimal rewards;
    private long fullParticipation;

}
