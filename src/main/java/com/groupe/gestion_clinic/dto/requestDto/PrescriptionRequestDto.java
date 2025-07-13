package com.groupe.gestion_clinic.dto.requestDto;

import lombok.*;

import java.time.LocalDate;



@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionRequestDto {

    private String posologie;
    private String dosage;
    private String medicament;
    private LocalDate prescriptionDate;
    private Boolean effective;
    private Integer rendezvousId;
}
