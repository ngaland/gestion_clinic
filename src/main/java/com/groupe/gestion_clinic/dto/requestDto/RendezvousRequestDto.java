package com.groupe.gestion_clinic.dto.requestDto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;

public class RendezvousRequestDto {

    @NotNull
    private Long patientId;

    @NotNull
    private Long medecinId;

    @Future
    @NotNull
    private LocalDateTime dateHeureDebut;

    @NotNull
    private Duration duree;

    private String motif;
    private String salle;
}
