package com.groupe.gestion_clinic.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Adresse {

    private String street ;

    private Integer houseNumber ;

    private Integer postalCode ;

    private String city ;

    private String country ;
}
