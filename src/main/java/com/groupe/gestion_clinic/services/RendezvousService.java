package com.groupe.gestion_clinic.services;

import com.groupe.gestion_clinic.dto.RendezvousDto;
import com.groupe.gestion_clinic.dto.RendezvousSearchDto;
import com.groupe.gestion_clinic.dto.requestDto.RendezvousRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface RendezvousService {
    RendezvousDto createRendezVous(RendezvousRequestDto requestDTO) ;

    RendezvousDto updateRendezVous(Integer id, RendezvousRequestDto requestDTO);

    void cancelRendezVous(Integer id) ;

    RendezvousDto getRendezVousById(Long id) ;

    List<RendezvousDto> getAllRendezVous();

    List<RendezvousDto> searchRendezVous(RendezvousSearchDto searchDTO);

    List<RendezvousDto> getUpcomingRendezVousForMedecin(Long medecinId);

    List<RendezvousDto> getRendezVousBetweenDates(LocalDateTime start, LocalDateTime end, Long medecinId);

    void checkRendezVousConflict(Long medecinId, LocalDateTime start, LocalDateTime end) ;
}
