package com.groupe.gestion_clinic.model;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Rendezvous extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "patientId")
    private Patient patient;

/*    @OneToMany(mappedBy = "rendezvous")
    private List<Facture> facture;*/

    @OneToMany(mappedBy = "rendezvous")
    private List<Prescription> prescription;

    @ManyToOne
    @JoinColumn(name = "secretaireId")
    private Secretaire secretaire;
}
