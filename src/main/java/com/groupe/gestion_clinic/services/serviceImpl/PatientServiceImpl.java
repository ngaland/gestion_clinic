package com.groupe.gestion_clinic.services.serviceImpl;

import com.groupe.gestion_clinic.dto.PatientDto;
import com.groupe.gestion_clinic.dto.PrescriptionDto;
import com.groupe.gestion_clinic.model.Patient;
import com.groupe.gestion_clinic.repositories.PatientRepository;
import com.groupe.gestion_clinic.services.PatientService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    public PatientDto createPatient(PatientDto patientDto) {

        if(patientRepository.findByEmail(patientDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Patient with email already exists");
        }
        return PatientDto.fromEntity(patientRepository.save(PatientDto.toDto(patientDto)));
    }

    @Override
    public PatientDto updatePatient(Integer id, PatientDto patientDto) {

        PatientDto dto =
                patientRepository
                        .findById(id)
                        .map(PatientDto::fromEntity)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("Patient with id " + id + " not found")
                        );

        dto.setAllergies(patientDto.getAllergies());
        dto.setEmail(patientDto.getEmail());
        dto.setTelephone(patientDto.getTelephone());
        dto.setNom(patientDto.getNom());
        dto.setPrenom(patientDto.getPrenom());

        return PatientDto.fromEntity(patientRepository.save(PatientDto.toDto(patientDto)));
    }

    @Override
    public PatientDto findById(Integer id) {
        return
                patientRepository
                        .findById(id)
                        .map(PatientDto::fromEntity)
                        .orElseThrow(()->new EntityNotFoundException("Patient with id " + id + " not found"));
    }

    @Override
    public List<PatientDto> findAll() {

        List<Patient>  patients = patientRepository.findAll();

        return
                Optional.of(patients)
                        .filter(elt-> !elt.isEmpty())
                        .orElseThrow(
                                ()-> new EntityNotFoundException("EMPTY LIST")
                        ).stream()
                        .map(PatientDto::fromEntity)
                        .toList();
    }


    @Override
    public PatientDto deletePatient(Integer id) {
        if(id == null) {
            return null;
        }

        Patient patient =
                        patientRepository
                                .findById(id)
                                .orElseThrow(
                                    ()->new EntityNotFoundException("Patient with id " + id + " not found")
                                );
        return PatientDto.fromEntity(patient);
    }

    @Override
    public List<PrescriptionDto> getPatientPrescriptionsHistory(Integer patientId) {
        return List.of();
    }
}
