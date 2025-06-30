package com.groupe.gestion_clinic.repositories;

import com.groupe.gestion_clinic.model.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedecinRepository extends JpaRepository<Medecin, Integer> {
}
