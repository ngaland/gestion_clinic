package com.groupe.gestion_clinic.controllers;

import com.groupe.gestion_clinic.dto.PatientDto;
import com.groupe.gestion_clinic.services.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;


    @PostMapping("/save")
    public ResponseEntity<PatientDto> savePatient( @RequestBody PatientDto patientDto) {
        return ResponseEntity.ok(patientService.createPatient(patientDto));
    }

    @PutMapping("/update/{patientId}")
    public ResponseEntity<PatientDto> updatePatient( @PathVariable Integer patientId, @RequestBody PatientDto patientDto) {
        return ResponseEntity.ok(patientService.updatePatient(patientId, patientDto));
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientDto> updatePatient( @PathVariable Integer patientId) {
        return ResponseEntity.ok(patientService.findById(patientId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PatientDto>> findAllPatient(){
        return ResponseEntity.ok(patientService.findAll());
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<?> deletePatient(@PathVariable Integer patientId) {
        patientService.deletePatient(patientId);
        return ResponseEntity.noContent().build();
    }


}
