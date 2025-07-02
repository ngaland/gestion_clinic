package com.groupe.gestion_clinic.services;

import com.groupe.gestion_clinic.dto.PatientDto;
import com.groupe.gestion_clinic.dto.PrescriptionDto;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    PatientDto createPatient(PatientDto patientDto);
    PatientDto updatePatient(Integer id ,PatientDto patientDto);
    PatientDto findById(Integer id);
    List<PatientDto> findAll();
    void deletePatient(Integer id);

    List<PrescriptionDto> getPatientPrescriptionsHistory(Integer patientId);

}
