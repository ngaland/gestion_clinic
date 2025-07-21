package com.groupe.gestion_clinic.controllers;

import com.groupe.gestion_clinic.dto.MedecinDto;
import com.groupe.gestion_clinic.dto.RendezvousDto;
import com.groupe.gestion_clinic.dto.RendezvousSearchDto;
import com.groupe.gestion_clinic.dto.requestDto.RendezvousRequestDto;
import com.groupe.gestion_clinic.services.RendezvousService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rendezvous")
@RequiredArgsConstructor
public class RendezvousController {

    private final RendezvousService rendezvousService;

    @PostMapping("/create")
    public ResponseEntity<RendezvousDto> createRendezVous(@RequestBody RendezvousRequestDto requestDto) {
        return ResponseEntity.ok(rendezvousService.createRendezVous(requestDto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RendezvousDto> updateRendezVous(@PathVariable Integer id, @RequestBody RendezvousRequestDto requestDto) {
        return ResponseEntity.ok(rendezvousService.updateRendezVous(id, requestDto));
    }


    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelRendezVous(@PathVariable Integer id) {
        return ResponseEntity.ok(rendezvousService.cancelRendezVous(id));
    }
    @DeleteMapping("/delete/{rendezvousId}")
    public ResponseEntity<Void> deleteRendezVous(@PathVariable Integer rendezvousId) {
        rendezvousService.deleteRendezVous(rendezvousId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RendezvousDto> getRendezVousById(@PathVariable Integer id) {
        return ResponseEntity.ok(rendezvousService.getRendezVousById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllRendezVous() {
        return ResponseEntity.ok(rendezvousService.getAllRendezVous());
    }

    @GetMapping("/all/upcoming")
    public ResponseEntity<List<RendezvousDto>> getUpcomingRendezVousForMedecin() {
        return ResponseEntity.ok(rendezvousService.getUpcomingRendezVousForMedecin());
    }


    @GetMapping("/between-dates")
    public ResponseEntity<List<RendezvousDto>> getRendezVousBetweenDates(
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
                                @RequestParam(required = false)  Integer medecinId) {
        return ResponseEntity.ok(rendezvousService.getRendezVousBetweenDates(start, end, medecinId));
    }


    @PostMapping("/all/search")
    public ResponseEntity<List<RendezvousDto>> searchRendezVous(@RequestBody RendezvousSearchDto searchDTO) {
        return ResponseEntity.ok(rendezvousService.searchRendezVous(searchDTO));
    }


}
