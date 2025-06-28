package com.groupe.gestion_clinic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Prescription extends AbstractEntity{


    private String posologie;

    @ManyToOne
    @JoinColumn(name = "rendezvousId")
    private Rendezvous rendezvous;

    @ManyToOne
    @JoinColumn(name = "factureId")
    private Facture facture;


}
