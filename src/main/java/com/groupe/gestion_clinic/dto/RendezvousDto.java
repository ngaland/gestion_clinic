package com.groupe.gestion_clinic.dto;

import com.groupe.gestion_clinic.model.Rendezvous;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RendezvousDto {

    private Integer id;

    private PatientDto patientDTO;

    private MedecinDto medecinDTO;

    private LocalDateTime dateHeureDebut;

    private LocalDateTime dateHeureFin;

    private String motif;

    private String salle;

    private String statut;

    public static RendezvousDto fromEntity(Rendezvous rendezvous) {
        return
                RendezvousDto.builder().build();
    }
    public static Rendezvous toDto(RendezvousDto rendezvousDto) {
        return
                Rendezvous
                        .builder()
                        .build();
    }

}
