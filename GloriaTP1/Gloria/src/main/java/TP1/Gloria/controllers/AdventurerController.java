package TP1.Gloria.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import TP1.Gloria.service.AdventurerService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/Adventurers")
@RequiredArgsConstructor
public class AdventurerController {
    

    private final AdventurerService adventurerService;

   @GetMapping("/{id}")
    public ResponseEntity<?> getById(@Positive @PathVariable Long id){
        return ResponseEntity.ok(adventurerService.getDataByID(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adventurerService.delete(id); // Preciso botar delete no do adventure
        return ResponseEntity.noContent().build(); 
    }


}
