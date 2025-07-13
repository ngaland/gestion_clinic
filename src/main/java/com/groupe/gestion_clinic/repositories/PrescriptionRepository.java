package com.groupe.gestion_clinic.repositories;

import com.groupe.gestion_clinic.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {

    // Trouver toutes les prescriptions pour un RendezVous ID
    List<Prescription> findByRendezvousId(Integer rendezvousId);

    // Trouver toutes les prescriptions pour une Facture ID
    List<Prescription> findByFactureId(Integer factureId);


}
