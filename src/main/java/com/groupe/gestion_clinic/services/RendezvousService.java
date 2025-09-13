package com.groupe.gestion_clinic.services;

import com.groupe.gestion_clinic.dto.RendezvousDto;
import com.groupe.gestion_clinic.dto.RendezvousSearchDto;
import com.groupe.gestion_clinic.dto.requestDto.RendezvousRequestDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RendezvousService {
    RendezvousDto createRendezVous(RendezvousRequestDto requestDTO) ;

    RendezvousDto updateRendezVous(Integer id, RendezvousRequestDto requestDTO);

    Void cancelRendezVous(Integer id) ;

    RendezvousDto getRendezVousById(Integer id) ;

    List<RendezvousDto> getAllRendezVous();
    List<RendezvousDto> getAllByMedecinAndPeriode(Integer medecinId,LocalDateTime startDate, LocalDateTime endDate);

    List<RendezvousDto> searchRendezVous(RendezvousSearchDto searchDTO);

    List<RendezvousDto> getUpcomingRendezVousForMedecin();

    List<RendezvousDto> getRendezVousBetweenDates(LocalDateTime start, LocalDateTime end, Integer medecinId);

    void checkRendezVousConflict(Integer medecinId, LocalDateTime start, LocalDateTime end) ;

    void deleteRendezVous(Integer id);

    List<RendezvousDto> getAllRendezVousByMedecin(Integer medecinId);

    List<RendezvousDto> getPlanifiedRendezVousByMedecin(Integer medecinId);

    List<RendezvousDto> getCancelledRendezVousByMedecin(Integer medecinId);

}
