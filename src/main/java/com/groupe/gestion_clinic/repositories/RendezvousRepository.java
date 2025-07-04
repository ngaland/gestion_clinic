package com.groupe.gestion_clinic.repositories;

import com.groupe.gestion_clinic.model.Rendezvous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RendezvousRepository extends JpaRepository<Rendezvous, Integer> {

// verifier si un médecin a des rendez-vous non annulés qui se chevauchent avec la plage horaire [start, end]
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Rendezvous r " +
            "WHERE r.medecin.id = :medecinId " +
            "AND r.statut <> 'ANNULE' " +
            "AND ((r.dateHeureDebut < :end AND r.dateHeureFin > :start))")
    boolean existsConflictingMedecinRendezVous(
                                                @Param("medecinId") Integer medecinId,
                                                @Param("start") LocalDateTime start,
                                                @Param("end") LocalDateTime end);

/*
    verifier si un patient a des rendez-vous non annulés prévus dans un intervale de temps
     pour le jour spécifié [heureJourDebut,heureJourFin]
 */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Rendezvous r " +
            "WHERE r.patient.id = :patientId " +
            "AND r.statut <> 'ANNULE' " +
            "AND r.dateHeureDebut BETWEEN :startOfDay AND :endOfDay")
    boolean existsByPatientAndDate(
                                    @Param("patientId") Long patientId,
                                    @Param("startOfDay") LocalDateTime startOfDay,
                                    @Param("endOfDay") LocalDateTime endOfDay);


/*   selectionner la liste des rendezvous de  tous les medecins ou pour un medecin specifique
     par ordre chronologique sur une periode definie
 */
    @Query("SELECT r FROM Rendezvous r " +
            "WHERE (:medecinId IS NULL OR r.medecin.id = :medecinId) " +
            "AND r.dateHeureDebut BETWEEN :start AND :end " +
            "ORDER BY r.dateHeureDebut")
    List<Rendezvous> findAllByMedecinAndPeriod(
                                                @Param("medecinId") Long medecinId,
                                                @Param("start") LocalDateTime start,
                                                @Param("end") LocalDateTime end);

/*
    afficher tous les rendevous qui ont le statut PLANIFIE qui se trouve entre une date initiale et une date future
* */
    @Query("SELECT r FROM Rendezvous r " +
            "WHERE r.dateHeureDebut BETWEEN :now AND :futureDate " +
            "AND r.statut = 'PLANIFIE'")
    List<Rendezvous> findUpcomingRendezVous(
            @Param("now") LocalDateTime now,
            @Param("futureDate") LocalDateTime futureDate);
}
