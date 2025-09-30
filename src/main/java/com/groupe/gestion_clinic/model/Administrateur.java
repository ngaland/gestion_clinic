package com.groupe.gestion_clinic.model;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@DiscriminatorValue("ADMIN")
@NoArgsConstructor
public class Administrateur extends Utilisateur {


}
