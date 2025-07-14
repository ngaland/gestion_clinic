package com.groupe.gestion_clinic.dto;

import com.groupe.gestion_clinic.model.Role;
import com.groupe.gestion_clinic.model.Secretaire;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SecretaireDto {
    private Integer id;
    private String nom;
    private String prenom;
    private String email;
    private Role role;
    private AdressDto adressDto;




    public static SecretaireDto fromEntity(Secretaire secretaire) {
        if (secretaire == null) return null;

        return SecretaireDto.builder()
                .id(secretaire.getId())
                .nom(secretaire.getNom())
                .prenom(secretaire.getPrenom())
                .email(secretaire.getEmail())
                .role(secretaire.getRole())
                .adressDto(AdressDto.fromEntity(secretaire.getAdresse()))
                .build();
    }

    public static Secretaire toDto(SecretaireDto dto) {
        if (dto == null) return null;

        return Secretaire.builder()
                .id(dto.getId())
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .email(dto.getEmail())
                .role(dto.getRole())
                .adresse(AdressDto.toDto(dto.getAdressDto()))
                .build();
    }
}
