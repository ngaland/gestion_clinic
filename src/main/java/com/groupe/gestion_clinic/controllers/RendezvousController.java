package com.groupe.gestion_clinic.controllers;

import com.groupe.gestion_clinic.dto.RendezvousSearchDto;
import com.groupe.gestion_clinic.dto.requestDto.RendezvousRequestDto;
import com.groupe.gestion_clinic.services.RendezvousService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/rendezvous")
@RequiredArgsConstructor
public class RendezvousController {

    private final RendezvousService rendezvousService;

    @PostMapping("/create")
    public ResponseEntity<?> createRendezVous(@RequestBody RendezvousRequestDto requestDto) {
        return ResponseEntity.ok(rendezvousService.createRendezVous(requestDto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRendezVous(@PathVariable Integer id, @RequestBody RendezvousRequestDto requestDto) {
        return ResponseEntity.ok(rendezvousService.updateRendezVous(id, requestDto));
    }


    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<?> cancelRendezVous(@PathVariable Integer id) {
        return ResponseEntity.ok(rendezvousService.cancelRendezVous(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRendezVousById(@PathVariable Integer id) {
        return ResponseEntity.ok(rendezvousService.getRendezVousById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllRendezVous() {
        return ResponseEntity.ok(rendezvousService.getAllRendezVous());
    }

    @GetMapping("/upcoming/{medecinId}")
    public ResponseEntity<?> getUpcomingRendezVousForMedecin(@PathVariable Integer medecinId) {
        return ResponseEntity.ok(rendezvousService.getUpcomingRendezVousForMedecin(medecinId));
    }


    @GetMapping("/between-dates")
    public ResponseEntity<?> getRendezVousBetweenDates(
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
                                @RequestParam(required = false)  Integer medecinId) {
        return ResponseEntity.ok(rendezvousService.getRendezVousBetweenDates(start, end, medecinId));
    }


    @GetMapping("/search")
    public ResponseEntity<?> searchRendezVous(@RequestBody RendezvousSearchDto searchDTO) {
        return ResponseEntity.ok(rendezvousService.searchRendezVous(searchDTO));
    }








}
