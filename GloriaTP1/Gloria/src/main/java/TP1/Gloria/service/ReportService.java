package TP1.Gloria.service;

import TP1.Gloria.dto.AdventurerRankingDTO;
import TP1.Gloria.repository.AdventurerRepository;
import TP1.Gloria.repository.MissionRepository;
import java.time.OffsetDateTime;

public class ReportService {
    private final AdventurerRepository adventurerRepository;
    private final MissionRepository missionRepository;


public ReportService(AdventurerRepository adventurerRepository, MissionRepository missionRepository) {
    this.adventurerRepository = adventurerRepository;
    this.missionRepository = missionRepository;
}

    
    public List<AdventurerRankingDTO> getAdventurerRank() 

}
