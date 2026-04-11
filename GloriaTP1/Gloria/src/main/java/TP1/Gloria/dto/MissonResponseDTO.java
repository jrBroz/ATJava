package TP1.Gloria.dto;
import lombok.Data;
import java.time.OffsetDateTime;

import TP1.Gloria.enums.DangerLevel;
import TP1.Gloria.enums.MissionStatus;


public class MissonResponseDTO {
    
    private Long id;
    private String missionTitle;
    private DangerLevel dangerLevel;
    private String organizationName;
    private MissionStatus missionStatus;
    private OffsetDateTime missionCreatedAt;

 
}
