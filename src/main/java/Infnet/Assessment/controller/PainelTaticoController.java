package Infnet.Assessment.controller;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import Infnet.Assessment.dto.PainelTaticoDTO.PainelTaticoResponseDTO;
import Infnet.Assessment.service.PainelTaticoMissaoService;

public class PainelTaticoController {


    private  PainelTaticoMissaoService service;

    @GetMapping("/top15dias")
    public ResponseEntity<List<PainelTaticoResponseDTO>> getTop15Dias() {
        return ResponseEntity.ok(service.obterTopMissoesTaticas());
    }
}
