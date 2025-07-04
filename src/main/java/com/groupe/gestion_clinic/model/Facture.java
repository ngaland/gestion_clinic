package com.groupe.gestion_clinic.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Facture extends AbstractEntity{


    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false)
    private LocalDate dateEmission;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutFacture statut;

    @OneToMany(mappedBy = "facture")
    private List<Prescription> prescription;
}
