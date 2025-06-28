package com.groupe.gestion_clinic.repositories;

import com.groupe.gestion_clinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
}
