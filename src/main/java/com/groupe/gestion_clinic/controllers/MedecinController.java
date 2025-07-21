package com.groupe.gestion_clinic.controllers;

import com.groupe.gestion_clinic.dto.MedecinDto;
import com.groupe.gestion_clinic.services.MedecinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medecin")
@RequiredArgsConstructor
public class MedecinController {
    private final MedecinService medecinService;


    @PostMapping("/save")
    public ResponseEntity<MedecinDto> saveMedecin(@RequestBody MedecinDto medecinDto) {
        return ResponseEntity.ok(medecinService.createMedecin(medecinDto));
    }

    @PutMapping("/{medecinId}")
    public ResponseEntity<MedecinDto> updateMedecin( @PathVariable Integer medecinId, @RequestBody MedecinDto medecinDto) {
        return ResponseEntity.ok(medecinService.updateMedecin(medecinId, medecinDto));
    }

    @GetMapping("/{medecinId}")
    public ResponseEntity<MedecinDto> findMedecinById( @PathVariable Integer medecinId) {
        return ResponseEntity.ok(medecinService.findById(medecinId));
    }


    @GetMapping("/all")
    public ResponseEntity<List<MedecinDto>> findAllMedecin(){
        return ResponseEntity.ok(medecinService.findAll());
    }

    @DeleteMapping("/{medecinId}")
    public ResponseEntity<Void> deleteMedecin(@PathVariable Integer medecinId) {
        medecinService.deleteMedecin(medecinId);
        return ResponseEntity.noContent().build();
    }
}
