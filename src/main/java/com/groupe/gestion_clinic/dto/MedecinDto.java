package com.groupe.gestion_clinic.dto;

import com.groupe.gestion_clinic.model.Adresse;
import com.groupe.gestion_clinic.model.Medecin;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedecinDto {


    private String nom;

    private String prenom;

    private String email;

    private String specialite;

    private AdressDto adressDto;

    public static MedecinDto fromEntity(Medecin medecin) {
        return MedecinDto.builder().build();
    }


    public static Medecin toDto(MedecinDto medecinDto) {
        return Medecin.builder().build();
    }
}
