package com.groupe.gestion_clinic.dto;


import com.groupe.gestion_clinic.model.Adresse;
import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdressDto {

    private String street ;

    private Integer houseNumber ;

    private Integer postalCode ;

    private String city ;

    private String country ;

    public static AdressDto fromEntity(Adresse adresse) {
        return AdressDto
                .builder()
                .city(adresse.getCity())
                .country(adresse.getCountry())
                .houseNumber(adresse.getHouseNumber())
                .postalCode(adresse.getPostalCode())
                .street(adresse.getStreet())
                .build();
    }


    public static Adresse toDto(AdressDto adressDto) {
        return Adresse
                .builder()
                .city(adressDto.getCity())
                .country(adressDto.getCountry())
                .houseNumber(adressDto.getHouseNumber())
                .postalCode(adressDto.getPostalCode())
                .street(adressDto.getStreet())
                .build();
    }
}
