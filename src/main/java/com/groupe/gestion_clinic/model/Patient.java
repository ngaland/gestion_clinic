package com.groupe.gestion_clinic.model;


import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
public class Patient extends AbstractEntity{

    private LocalDate dateNaissance;

    private String telephone;

    private String antecedents;

    private String allergies;

    @Embedded
    private Adresse adresse;

    @OneToMany(mappedBy = "patient")
    private List<Rendezvous> rendezvouses;

}
