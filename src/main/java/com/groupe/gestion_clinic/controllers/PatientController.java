package com.groupe.gestion_clinic.controllers;

import com.groupe.gestion_clinic.dto.PatientDto;
import com.groupe.gestion_clinic.services.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;


    @PostMapping("/save")
    public ResponseEntity<?> savePatient( @RequestBody PatientDto patientDto) {
        return ResponseEntity.ok(patientService.createPatient(patientDto));
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<?> updatePatient( @PathVariable Integer patientId, @RequestBody PatientDto patientDto) {
        return ResponseEntity.ok(patientService.updatePatient(patientId, patientDto));
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAllPatient(){
        return ResponseEntity.ok(patientService.findAll());
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<?> deletePatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(patientService.deletePatient(patientId));
    }


}
