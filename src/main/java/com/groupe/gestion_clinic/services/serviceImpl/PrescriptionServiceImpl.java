package com.groupe.gestion_clinic.services.serviceImpl;

import com.groupe.gestion_clinic.dto.PrescriptionDto;
import com.groupe.gestion_clinic.dto.requestDto.PrescriptionRequestDto;
import com.groupe.gestion_clinic.exceptions.BusinessException;
import com.groupe.gestion_clinic.exceptions.NotFoundException;
import com.groupe.gestion_clinic.model.Medecin;
import com.groupe.gestion_clinic.model.Patient;
import com.groupe.gestion_clinic.model.Prescription;
import com.groupe.gestion_clinic.model.Rendezvous;
import com.groupe.gestion_clinic.notificationConfig.NotificationService;
import com.groupe.gestion_clinic.repositories.PrescriptionRepository;
import com.groupe.gestion_clinic.repositories.RendezvousRepository;
import com.groupe.gestion_clinic.services.PrescriptionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final RendezvousRepository rendezvousRepository;
    private final NotificationService notificationService;

    @Override
    public PrescriptionDto createPrescription(PrescriptionRequestDto requestDto) {


        Rendezvous rendezvous = rendezvousRepository.findById(requestDto.getRendezvousId())
                .orElseThrow(() -> new NotFoundException("Rendezvous non trouvé avec l'ID : " + requestDto.getRendezvousId()));

        // Le médecin et le patient
        Medecin medecin = rendezvous.getMedecin();
        Patient patient = rendezvous.getPatient();

        if (medecin == null || patient == null) {
            throw new BusinessException("Le Rendezvous associé doit avoir un médecin et un patient.");
        }

        if(prescriptionRepository.existsByRendezvousIdAndMedicamentAndDosageAndPosologie(
                                                        requestDto.getRendezvousId(),
                                                        requestDto.getMedicament(),
                                                        requestDto.getDosage(),
                                                        requestDto.getPosologie())
        )
        {
            throw new BusinessException("Cette prescription existe déjà pour ce rendez-vous.");
        }

        Prescription prescription = new Prescription();
        prescription.setPrescriptionDate(requestDto.getPrescriptionDate() != null ? requestDto.getPrescriptionDate() : LocalDate.now());
        prescription.setMedicament(requestDto.getMedicament()); // Nouveau champ
        prescription.setPosologie(requestDto.getPosologie()); // Nouveau champ
        prescription.setDosage(requestDto.getDosage());
        prescription.setEffective(requestDto.getEffective() != null ? requestDto.getEffective() : true);
        prescription.setRendezvous(rendezvous);

        // la facture n'est pas definit a la creation de la prescription
        Prescription savedPrescription = prescriptionRepository.save(prescription);

        return PrescriptionDto.fromEntity(savedPrescription);
    }

    @Override
    public PrescriptionDto getPrescriptionById(Integer id) {
        return
                prescriptionRepository
                        .findById(id)
                        .map(PrescriptionDto::fromEntity)
                        .orElseThrow(()-> new NotFoundException("Prescription non trouvée avec l'ID : " + id));
    }

    @Override
    public PrescriptionDto updatePrescription(Integer id, PrescriptionRequestDto requestDto) {

        Prescription existingPrescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prescription non trouvée avec l'ID : " + id));

        //On s’assure que le champ rendezvousId est non null
        //On compare l’ID du rendez-vous actuel de l'ordonnance (existingPrescription) avec celui fourni dans le DTO.
        //si les ID sont Differents , alors l'utilisateur veut changer le rendez-vous associé à cette prescription.
        if (requestDto.getRendezvousId() != null && !requestDto.getRendezvousId().equals(existingPrescription.getRendezvous().getId())) {
            Rendezvous newRendezvous = rendezvousRepository.findById(requestDto.getRendezvousId())
                    .orElseThrow(() -> new NotFoundException("Nouveau Rendezvous non trouvé."));
            existingPrescription.setRendezvous(newRendezvous);
        }

        existingPrescription.setPrescriptionDate(requestDto.getPrescriptionDate() != null ? requestDto.getPrescriptionDate() : existingPrescription.getPrescriptionDate());
        existingPrescription.setMedicament(requestDto.getMedicament() != null ? requestDto.getMedicament() : existingPrescription.getMedicament());
        existingPrescription.setPosologie(requestDto.getPosologie() != null ? requestDto.getPosologie() : existingPrescription.getPosologie());
        existingPrescription.setDosage(requestDto.getDosage() != null ? requestDto.getDosage() : existingPrescription.getDosage());
        existingPrescription.setEffective(requestDto.getEffective() != null ? requestDto.getEffective() : existingPrescription.getEffective());

        Prescription updatedPrescription = prescriptionRepository.save(existingPrescription);
        return PrescriptionDto.fromEntity(updatedPrescription);
    }

    @Override
    public void deletePrescription(Integer id) {
        Prescription prescription = prescriptionRepository.findById(id).orElseThrow(() -> new NotFoundException("Prescription non trouvée avec l'ID : " + id));
        prescriptionRepository.delete(prescription);

    }

    @Override
    public List<PrescriptionDto> getAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        return Optional.of(prescriptions)
                .filter(elt-> !elt.isEmpty())
                .orElseThrow(
                        ()-> new EntityNotFoundException("EMPTY LIST")
                ).stream()
                .map(PrescriptionDto::fromEntity)
                .toList();
    }

    @Override
    public List<PrescriptionDto> getPrescriptionsByRendezvousId(Integer rendezvousId) {
        List<Prescription> prescriptions = prescriptionRepository.findByRendezvousId(rendezvousId);
        return Optional.of(prescriptions)
                .filter(elt-> !elt.isEmpty())
                .orElseThrow(
                        ()-> new EntityNotFoundException("EMPTY LIST")
                ).stream()
                .map(PrescriptionDto::fromEntity)
                .toList();
    }

    @Override
    public byte[] generatePrescriptionPdf(Integer prescriptionId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new NotFoundException("Prescription non trouvée pour générer le PDF avec l'ID : " + prescriptionId));

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                PDType1Font fontBold = PDType1Font.HELVETICA_BOLD;
                PDType1Font font = PDType1Font.HELVETICA;

                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - margin;
                float yPosition = yStart;
                float leading = 18; // Line spacing

                // Titre
                contentStream.beginText();
                contentStream.setFont(fontBold, 24);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Prescription Médicale");
                contentStream.endText();
                yPosition -= leading * 2;

                // Informations de base
                Medecin medecin = prescription.getRendezvous().getMedecin();
                Patient patient = prescription.getRendezvous().getPatient();

                contentStream.beginText();
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Date: " + prescription.getPrescriptionDate());
                yPosition -= leading;
                contentStream.newLineAtOffset(0, -leading);
                contentStream.showText("Médecin: Dr. " + medecin.getPrenom() + " " + medecin.getNom());
                yPosition -= leading;
                contentStream.newLineAtOffset(0, -leading);
                contentStream.showText("Patient: " + patient.getPrenom() + " " + patient.getNom());
                yPosition -= leading * 2;
                contentStream.endText();

                // Détails de la prescription
                addPrescriptionDetail(contentStream, fontBold, font, margin, yPosition, leading,
                        "Médicament:", prescription.getMedicament());
                yPosition -= leading * 2;

                addPrescriptionDetail(contentStream, fontBold, font, margin, yPosition, leading,
                        "Dosage:", prescription.getDosage());
                yPosition -= leading * 2;

                addPrescriptionDetail(contentStream, fontBold, font, margin, yPosition, leading,
                        "Posologie:", prescription.getPosologie());
                yPosition -= leading * 2;

                /*addPrescriptionDetail(contentStream, fontBold, font, margin, yPosition, leading,
                        "Instructions:",
                        prescription.getInstructions() != null ? prescription.getInstructions() : "N/A");*/
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            throw new BusinessException("Erreur lors de la génération du PDF de la prescription : " + e.getMessage());
        }
    }

    private void addPrescriptionDetail(PDPageContentStream contentStream,
                                       PDType1Font fontBold, PDType1Font font,
                                       float margin, float yPosition, float leading,
                                       String title, String value) throws IOException {
        contentStream.beginText();
        contentStream.setFont(fontBold, 14);
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText(title);
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(font, 12);
        contentStream.newLineAtOffset(margin, yPosition - leading);
        contentStream.showText(value != null ? value : "N/A");
        contentStream.endText();
    }
}

