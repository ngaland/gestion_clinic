package com.groupe.gestion_clinic.repositories;

import com.groupe.gestion_clinic.model.Rendezvous;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RendezvousRepository extends JpaRepository<Rendezvous, Integer> {
}
