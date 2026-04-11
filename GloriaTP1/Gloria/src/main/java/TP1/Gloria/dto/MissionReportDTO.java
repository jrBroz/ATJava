package TP1.Gloria.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

import TP1.Gloria.enums.DangerLevel;
import TP1.Gloria.enums.MissionStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MissionReportDTO {
    
    private String missionTitle;
    private MissionStatus status;
    private DangerLevel dangerLevel;
    private Long quantityParticipants;
    private BigDecimal allRewards;
}
