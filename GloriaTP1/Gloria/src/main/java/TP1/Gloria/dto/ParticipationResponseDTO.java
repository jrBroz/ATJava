package TP1.Gloria.dto;

import java.time.OffsetDateTime;

import TP1.Gloria.enums.MissionRole;
public class ParticipationResponseDTO {

    private String missionTitle;
    private String adventurerName;
    
    private MissionRole role; // papel do player na missao
    private OffsetDateTime registeredDate;
    
}
