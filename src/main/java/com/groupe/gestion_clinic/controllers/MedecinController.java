package com.groupe.gestion_clinic.controllers;

import com.groupe.gestion_clinic.dto.MedecinDto;
import com.groupe.gestion_clinic.services.MedecinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medecin")
@RequiredArgsConstructor
public class MedecinController {
    private final MedecinService medecinService;

    @PostMapping("/save")
    public ResponseEntity<?> saveMedecin(@RequestBody MedecinDto medecinDto) {
        return ResponseEntity.ok(medecinService.createMedecin(medecinDto));
    }

    @GetMapping("/{medecinId}")
    public ResponseEntity<?> updateMedecin( @PathVariable Integer medecinId, @RequestBody MedecinDto medecinDto) {
        return ResponseEntity.ok(medecinService.updateMedecin(medecinId, medecinDto));
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAllMedecin(){
        return ResponseEntity.ok(medecinService.findAll());
    }

    @DeleteMapping("/{medecinId}")
    public ResponseEntity<?> deleteMedecin(@PathVariable Integer medecinId) {
        return ResponseEntity.ok(medecinService.deleteMedecin(medecinId));
    }
}
