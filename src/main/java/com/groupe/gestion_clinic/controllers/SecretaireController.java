package com.groupe.gestion_clinic.controllers;

import com.groupe.gestion_clinic.dto.SecretaireDto;
import com.groupe.gestion_clinic.repositories.SecretaireRepository;
import com.groupe.gestion_clinic.services.SecretaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/secretaire")
@RequiredArgsConstructor
public class SecretaireController {

    private final SecretaireService secretaireService;

    @PostMapping("/save")
    public ResponseEntity<SecretaireDto> saveSecretaire(@RequestBody SecretaireDto dto) {
        SecretaireDto secretaireDto = secretaireService.createSecretaire(dto);
        return new ResponseEntity<>(secretaireDto, HttpStatus.CREATED);
    }

    @PutMapping("/{secretaireId}")
    public ResponseEntity<SecretaireDto> updateSecretaire(@PathVariable Integer secretaireId, @RequestBody SecretaireDto dto) {
        return ResponseEntity.ok(secretaireService.updateSecretaire(secretaireId, dto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SecretaireDto>> findAllSecretaire() {
        return ResponseEntity.ok(secretaireService.findAll());
    }

    @DeleteMapping("/{secretaireId}")
    public ResponseEntity<?> deleteSecretaire(@PathVariable Integer secretaireId) {
        secretaireService.deleteSecretaire(secretaireId);
        return ResponseEntity.noContent().build();
    }

}
