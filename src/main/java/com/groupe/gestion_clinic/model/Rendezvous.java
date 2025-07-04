package com.groupe.gestion_clinic.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Rendezvous extends AbstractEntity {


    private LocalDateTime dateHeureDebut;

    private LocalDateTime dateHeureFin;

    private String motif;

    private String salle;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "patientId")
    private Patient patient;

    private StatutRendezVous statut;

    @OneToMany(mappedBy = "rendezvous")
    private List<Prescription> prescription;

    @ManyToOne
    @JoinColumn(name = "secretaireId")
    private Secretaire secretaire;

    @ManyToOne
    @JoinColumn(name = "medecinId")
    private Medecin medecin;
}
