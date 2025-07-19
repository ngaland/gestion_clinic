package com.groupe.gestion_clinic.dto;

import com.groupe.gestion_clinic.model.Patient;
import com.groupe.gestion_clinic.model.Prescription;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDto {

    private Integer id;

    private String posologie;

    private String dosage;

    private String medicament;

    private LocalDate prescriptionDate;

    private Boolean effective;

    private Integer rendezvousId;

    private Integer factureId;



    public static PrescriptionDto fromEntity(Prescription prescription) {
        return
                PrescriptionDto
                        .builder()
                        .id(prescription.getId())
                        .dosage(prescription.getDosage())
                        .posologie(prescription.getPosologie())
                        .medicament(prescription.getMedicament())
                        .rendezvousId(prescription.getRendezvous().getId())
                        .factureId(prescription.getFacture() != null ? prescription.getFacture().getId() : null)
                        .effective(prescription.getEffective())
                        .build();

    }


}
