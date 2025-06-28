package com.groupe.gestion_clinic.repositories;

import com.groupe.gestion_clinic.model.Facture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactureRepository extends JpaRepository<Facture, Integer> {
}
