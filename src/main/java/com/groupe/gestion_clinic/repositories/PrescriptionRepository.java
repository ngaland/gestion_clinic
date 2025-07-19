package com.groupe.gestion_clinic.repositories;

import com.groupe.gestion_clinic.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {

    // Trouver toutes les prescriptions pour un RendezVous ID
    List<Prescription> findByRendezvousId(Integer rendezvousId);


    // Trouver toutes les prescriptions pour une Facture ID
    List<Prescription> findByFactureId(Integer factureId);


    // vérifier si une prescription existe déjà dans la base avec les critères suivants :
    // endezvousId, medicament,dosage,posologie
    boolean existsByRendezvousIdAndMedicamentAndDosageAndPosologie(
            Integer rendezvousId, String medicament, String dosage, String posologie
    );

}
