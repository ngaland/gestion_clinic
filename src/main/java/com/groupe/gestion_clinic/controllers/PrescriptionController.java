package com.groupe.gestion_clinic.controllers;

import com.groupe.gestion_clinic.dto.PrescriptionDto;
import com.groupe.gestion_clinic.dto.requestDto.PrescriptionRequestDto;
import com.groupe.gestion_clinic.services.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescription")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping("/create")
    public ResponseEntity<PrescriptionDto> createPrescription(@RequestBody PrescriptionRequestDto requestDto) {
        PrescriptionDto createdPrescription = prescriptionService.createPrescription(requestDto);
        return new ResponseEntity<>(createdPrescription, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPrescriptionById(@PathVariable Integer id) {
        PrescriptionDto prescription = prescriptionService.getPrescriptionById(id);
        return ResponseEntity.ok(prescription);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PrescriptionDto> updatePrescription(@PathVariable Integer id, @RequestBody PrescriptionRequestDto requestDto) {
        PrescriptionDto updatedPrescription = prescriptionService.updatePrescription(id, requestDto);
        return ResponseEntity.ok(updatedPrescription);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Integer id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/all")
    public ResponseEntity<List<PrescriptionDto>> getAllPrescriptions() {
        List<PrescriptionDto> prescriptions = prescriptionService.getAllPrescriptions();
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/by-rendezvous/{rendezvousId}")
    public ResponseEntity<List<PrescriptionDto>> getPrescriptionsByRendezvousId(@PathVariable Integer rendezvousId) {
        List<PrescriptionDto> prescriptions = prescriptionService.getPrescriptionsByRendezvousId(rendezvousId);
        return ResponseEntity.ok(prescriptions);
    }

    //  générer et télécharger une ordonnance (prescription) en format PDF.
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getPrescriptionPdf(@PathVariable Integer id) {
        /*
        * Appel du service generatePrescriptionPdf, qui doit "
        *  Récupérer la prescription depuis la base de données
        *  Générer un PDF avec PDFBox
        *  Retourner le contenu du PDF sous forme de byte[
        *
         * */
        byte[] pdfBytes = prescriptionService.generatePrescriptionPdf(id);

        /*
        * Indique au navigateur ou au client  que le contenu est un fichier PDF
         * */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = "prescription_" + id + ".pdf";

        // Téléchargement
        // setContentDispositionFormData() :force le navigateur à télécharger le fichier au lieu de l’ouvrir.
        headers.setContentDispositionFormData("attachment", filename);

        // contrôle la mise en cache, pour s'assurer que le navigateur télécharge toujours la version la plus récente.
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

}
