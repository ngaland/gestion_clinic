package com.groupe.gestion_clinic.dto;


import com.groupe.gestion_clinic.model.Facture;
import com.groupe.gestion_clinic.model.StatutFacture;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FactureDto {

    private Integer id;

    private Double montantTotal;

    private LocalDate dateEcheance;

    private StatutFacture statut;

    private String numeroFacture;

    private List<PrescriptionDto> prescriptionDtos;

    public static FactureDto fromEntity(Facture facture) {
        return
                FactureDto
                        .builder()
                        .numeroFacture(facture.getNumeroFacture())
                        .dateEcheance(facture.getDateEcheance())
                        .montantTotal(facture.getMontantTotal())
                        .statut(facture.getStatut())
                        .montantTotal(facture.getMontantTotal())
                        .prescriptionDtos(facture.getPrescription().stream().map(PrescriptionDto::fromEntity).toList()).build();
    }
}
