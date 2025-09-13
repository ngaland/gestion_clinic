package com.groupe.gestion_clinic.services.serviceImpl;

import com.groupe.gestion_clinic.dto.NotificationDto;
import com.groupe.gestion_clinic.dto.RendezvousDto;
import com.groupe.gestion_clinic.dto.RendezvousSearchDto;
import com.groupe.gestion_clinic.dto.requestDto.RendezvousRequestDto;
import com.groupe.gestion_clinic.exceptions.*;
import com.groupe.gestion_clinic.model.Medecin;
import com.groupe.gestion_clinic.model.Patient;
import com.groupe.gestion_clinic.model.Rendezvous;
import com.groupe.gestion_clinic.model.StatutRendezVous;
import com.groupe.gestion_clinic.notificationConfig.EmailService;
import com.groupe.gestion_clinic.notificationConfig.NotificationService;
import com.groupe.gestion_clinic.repositories.MedecinRepository;
import com.groupe.gestion_clinic.repositories.PatientRepository;
import com.groupe.gestion_clinic.repositories.RendezvousRepository;
import com.groupe.gestion_clinic.services.RendezvousService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.groupe.gestion_clinic.utils.DateTimeUtils.formatDateTime;


@Service
@RequiredArgsConstructor
public class RendezvousServiceImpl implements RendezvousService {

    private final RendezvousRepository rendezvousRepository;
    private final PatientRepository patientRepository;
    private final MedecinRepository medecinRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;


    @Override
    public RendezvousDto createRendezVous(RendezvousRequestDto requestDto) {

        // recuperer patient et medecin s'ils existent
        Patient patient = patientRepository.findById(requestDto.getPatientId())
                .orElseThrow(() -> new NotFoundException("Patient non trouvé"));

        Medecin medecin = medecinRepository.findById(requestDto.getMedecinId())
                .orElseThrow(() -> new NotFoundException("Médecin non trouvé"));

        LocalDateTime start = requestDto.getDateHeureDebut();
        LocalDateTime end = start.plus(requestDto.getDuree());

        // Vérification des conflits
        checkRendezVousConflict(requestDto.getMedecinId(), start, end);
        checkPatientAvailability(requestDto.getPatientId(), start.toLocalDate());

        // Création du rendez-vous
        Rendezvous rendezVous = new Rendezvous();
        rendezVous.setPatient(patient);
        rendezVous.setMedecin(medecin);
        rendezVous.setDateHeureDebut(start);
        rendezVous.setDateHeureFin(end);
        rendezVous.setMotif(requestDto.getMotif());
        rendezVous.setSalle(requestDto.getSalle());
        rendezVous.setStatut(StatutRendezVous.PLANIFIER);

        // sauvegarder le rendezvous
        Rendezvous savedRendezVous = rendezvousRepository.save(rendezVous);

        // Notification
        // todo ..................

        // Notifier le médecin
        notificationService.sendPrivateNotification(
                                                    medecin.getId().longValue(),
                                                    new NotificationDto(
                                                            "NEW_RDV",
                                                            "Nouveau RDV avec " + patient.getNom() + " " + patient.getPrenom() +
                                                                    " le " + formatDateTime(start) + " en salle " + requestDto.getSalle(),
                                                            savedRendezVous.getId(),
                                                            LocalDateTime.now(),
                                                            "MEDECIN",
                                                            medecin.getId().longValue()
                                                    )
        );

        // Notifier le patient
        notificationService.sendPrivateNotification(
                patient.getId().longValue(),
                                            new NotificationDto(
                                                    "NEW_RDV",
                                                    "Votre RDV avec Dr. " + medecin.getNom() +
                                                            " est confirmé pour le " + formatDateTime(start) +
                                                            " en salle " + requestDto.getSalle(),
                                                    savedRendezVous.getId(),
                                                    LocalDateTime.now(),
                                                    "PATIENT",
                                                    patient.getId().longValue()
                                            )
        );

/*        // Notification publique pour les secrétaires
        notificationService.sendPublicNotification(
                                            new NotificationDto(
                                                    "NEW_RDV",
                                                    "Nouveau RDV programmé: " + patient.getNom() + " avec Dr. " +
                                                            medecin.getNom() + " le " + formatDateTime(start),
                                                    savedRendezVous.getId(),
                                                    LocalDateTime.now(),
                                                    "SECRETAIRE",
                                                    null
                                            )
        );*/
        return RendezvousDto.fromEntity(savedRendezVous);
    }


    @Override
    public RendezvousDto updateRendezVous(Integer id, RendezvousRequestDto requestDto) {

        Rendezvous existingRendezVous = rendezvousRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rendez-vous non trouvé"));

        Patient patient = patientRepository.findById(requestDto.getPatientId())
                .orElseThrow(() -> new NotFoundException("Patient non trouvé"));

        Medecin medecin = medecinRepository.findById(requestDto.getMedecinId())
                .orElseThrow(() -> new NotFoundException("Médecin non trouvé"));

        LocalDateTime newStart = requestDto.getDateHeureDebut();
        LocalDateTime newEnd = newStart.plus(requestDto.getDuree());

        // Vérification si modification des dates
        if (!existingRendezVous.getDateHeureDebut().equals(requestDto.getDateHeureDebut())) {

            checkRendezVousConflict(requestDto.getMedecinId(), newStart, newEnd);
            checkPatientAvailability(requestDto.getPatientId(), newStart.toLocalDate());
        }

        // Sauvegarde avant notification pour avoir l'état final
        existingRendezVous.setPatient(patient);
        existingRendezVous.setMedecin(medecin);
        existingRendezVous.setDateHeureDebut(newStart);
        existingRendezVous.setDateHeureFin(newEnd);
        existingRendezVous.setMotif(requestDto.getMotif());
        existingRendezVous.setSalle(requestDto.getSalle());
        Rendezvous updatedRdv = rendezvousRepository.save(existingRendezVous);

        /*
            Notification
         */
        // todo ..................
        String updateMessage = "RDV modifié: " + formatDateTime(newStart) + " en salle " + requestDto.getSalle();

        // Pour le médecin
        notificationService.sendPrivateNotification(
                medecin.getId().longValue(),
                new NotificationDto(
                        "RDV_UPDATED",
                        updateMessage,
                        updatedRdv.getId(),
                        LocalDateTime.now(),
                        "MEDECIN",
                        medecin.getId().longValue()
                )
        );

        // Pour le patient
        notificationService.sendPrivateNotification(
                patient.getId().longValue(),
                new NotificationDto(
                        "RDV_UPDATED",
                        updateMessage,
                        updatedRdv.getId(),
                        LocalDateTime.now(),
                        "PATIENT",
                        patient.getId().longValue()
                )
        );

/*
        // Pour les secrétaires
        notificationService.sendPublicNotification(
                new NotificationDto(
                        "RDV_UPDATED",
                        "RDV #" + updatedRdv.getId() + " modifié",
                        updatedRdv.getId(),
                        LocalDateTime.now(),
                        "SECRETAIRE",
                        null
                )
        );*/


        return RendezvousDto.fromEntity(updatedRdv);
    }

    @Override
    public Void cancelRendezVous(Integer id) {

        Rendezvous rendezVous = rendezvousRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rendez-vous non trouvé"));

        LocalDateTime now = LocalDateTime.now();
        /*
         * Annulation impossible moins de 24h avant le rendez-vous”
         * "le rendez-vous est demain à 14h
         *       aujourd’hui il est 13h ==> annulation possible
         *
         * " le rendez-vous est demain à 14h
         *        aujourd’hui il est 15h ==> annulation impossible
         *
         * */
        // Rendez-vous Moins de 24h avant le début
        if (now.isAfter(rendezVous.getDateHeureDebut().minusHours(24))) {
            throw new TooLateToCancelException("Annulation impossible moins de 24h avant le rendez-vous") ;
        }

        // Rendez-vous déjà passé
        if (now.isAfter(rendezVous.getDateHeureFin())) {
            throw new PastAppointmentException("Impossible d'annuler un rendez-vous déjà passé");
        }

        // 2. Rendez-vous En cours ?
        if (now.isAfter(rendezVous.getDateHeureDebut()) && now.isBefore(rendezVous.getDateHeureFin())) {
            throw new BusinessException("Impossible d'annuler un rendez-vous en cours ....");
        }

        // Vérification que le RDV n'est pas déjà annulé ou terminé
        if (rendezVous.getStatut() != StatutRendezVous.PLANIFIER) {
            throw new BusinessException("Seuls les rendez-vous planifiés peuvent être annulés");
        }


        rendezVous.setStatut(StatutRendezVous.ANNULER);
        rendezVous.setDateAnnulation(LocalDateTime.now());
        Rendezvous cancelledRdv = rendezvousRepository.save(rendezVous);

         /*
            Notification
         */
        // todo ..................

        String cancelMessage = "RDV du " + formatDateTime(rendezVous.getDateHeureDebut()) + " annulé";
        // Pour le médecin
        notificationService.sendPrivateNotification(
                cancelledRdv.getMedecin().getId().longValue(),
                new NotificationDto(
                        "RDV_CANCELLED",
                        cancelMessage,
                        cancelledRdv.getId(),
                        LocalDateTime.now(),
                        "MEDECIN",
                        cancelledRdv.getMedecin().getId().longValue()
                )
        );

        // Pour le patient
        notificationService.sendPrivateNotification(
                cancelledRdv.getPatient().getId().longValue(),
                new NotificationDto(
                        "RDV_CANCELLED",
                        cancelMessage,
                        cancelledRdv.getId(),
                        LocalDateTime.now(),
                        "PATIENT",
                        cancelledRdv.getPatient().getId().longValue()
                )
        );

        // Pour les secrétaires
        notificationService.sendPublicNotification(
                new NotificationDto(
                        "RDV_CANCELLED",
                        "RDV #" + cancelledRdv.getId() + " annulé",
                        cancelledRdv.getId(),
                        LocalDateTime.now(),
                        "SECRETAIRE",
                        null
                )
        );

        // Email d'annulation
        emailService.sendCancellationEmail(
                cancelledRdv.getPatient().getEmail(),
                "Annulation de RDV",
                cancelMessage
        );


        return null ;
    }

    @Override
    public RendezvousDto getRendezVousById(Integer id) {

        return rendezvousRepository.findById(id)
                                    .map(RendezvousDto::fromEntity)
                                    .orElseThrow(
                                            ()-> new NotFoundException("Aucun Rendez-vous avec l'ID : "+id)
                                    );
    }

    @Override
    public List<RendezvousDto> getAllRendezVous() {

        List<Rendezvous>  rendezvousList = rendezvousRepository.findAll();

        return
                Optional.of(rendezvousList)
                        .filter(elt-> !elt.isEmpty())
                        .orElseThrow(
                                ()-> new NotFoundException("EMPTY LIST")
                        ).stream()
                        .map(RendezvousDto::fromEntity)
                        .toList();
    }

    @Override
    public List<RendezvousDto> getAllByMedecinAndPeriode(Integer medecinId,LocalDateTime startDate, LocalDateTime endDate) {
        List<Rendezvous>  rendezvousList = rendezvousRepository.findAllByMedecinAndPeriod(medecinId,startDate,endDate);
        return
                Optional.of(rendezvousList)
                    .filter(elt-> !elt.isEmpty())
                    .orElseThrow(
                            ()-> new NotFoundException("EMPTY LIST")
                    ).stream()
                    .map(RendezvousDto::fromEntity)
                    .toList();
    }


    @Override
    public void deleteRendezVous(Integer id) {
        /*
         *
         * si la date de debut du rendezvous est superieur a celle d'aujourd'hui il y'a i an
         * alors cela fait moins d'un ans que le Rendezvous a ete creer,sinon cela fait au moins 1 an que le Rendezvous a ete creer
         * NB : On ne peut supprimer que les RDV anciens (par exemple > 1 an)
         * */

        Rendezvous rendezVous = rendezvousRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Rendez-vous non trouvé")
                );


        if (rendezVous.getDateHeureDebut().isAfter(LocalDateTime.now().minusYears(1))) {
            throw new BusinessException("Seuls les rendez-vous de plus d'un an peuvent être supprimés");
        }

        rendezvousRepository.delete(rendezVous);
        /*
            Notification
         */
        // todo ..................

        // Notification avant suppression
        notificationService.sendPublicNotification(
                new NotificationDto(
                        "RDV_DELETED",
                        "RDV #" + rendezVous.getId() + " supprimé de l'historique",
                        rendezVous.getId(),
                        LocalDateTime.now(),
                        "SECRETAIRE",
                        null
                )
        );
    }


    @Override
    public List<RendezvousDto> searchRendezVous(RendezvousSearchDto searchDTO) {

        /*
        * permet de chercher des rendez-vous dynamiquement, selon plusieurs critères optionnels :
        *           un medecin,une date,un statut,une salle
        *
        * */

        //Specifivation<T> sert à construire des requêtes dynamiques sans écrire du SQL en dur
        // (pratique pour des requêtes multi-critères où tous les critères sont optionnels)
        Specification<Rendezvous> spec = (root, query, cb) -> cb.conjunction();

        if (searchDTO.getMedecinId() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("medecin").get("id"), searchDTO.getMedecinId()));
        }

        if (searchDTO.getDate() != null) {
            LocalDateTime startOfDay = searchDTO.getDate().atStartOfDay();
            LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
            spec = spec.and((root, query, cb) ->
                    cb.between(root.get("dateHeureDebut"), startOfDay, endOfDay));
        }

        if (searchDTO.getSalle() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("salle"), searchDTO.getSalle()));
        }

        if (searchDTO.getStatut() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("statut"), StatutRendezVous.valueOf(searchDTO.getStatut())));
        }

        return rendezvousRepository.findAll(spec)
                                    .stream()
                                    .map(RendezvousDto::fromEntity)
                                    .toList();
    }

    @Override
    public List<RendezvousDto> getUpcomingRendezVousForMedecin() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime inOneMonth = now.plusMonths(1);

        return rendezvousRepository.findUpcomingRendezVous(now,inOneMonth)
                                    .stream()
                                    .map(RendezvousDto::fromEntity)
                                    .toList();
    }

    @Override
    public List<RendezvousDto> getRendezVousBetweenDates(LocalDateTime start, LocalDateTime end, Integer medecinId) {
        return rendezvousRepository.findAllByMedecinAndPeriod(medecinId,start,end).stream().map((RendezvousDto::fromEntity)).toList();
    }

    @Override
    public void checkRendezVousConflict(Integer medecinId, LocalDateTime start, LocalDateTime end) {

        if (rendezvousRepository.existsConflictingMedecinRendezVous(medecinId, start, end)) {
            throw new ConflictException("Le médecin a déjà un rendez-vous pendant cette plage horaire");
        }

    }



    @Override
    public List<RendezvousDto> getAllRendezVousByMedecin(Integer medecinId) {
        return rendezvousRepository.findAllByMedecin(medecinId)
                .stream()
                .map(RendezvousDto::fromEntity)
                .toList();
    }

    @Override
    public List<RendezvousDto> getPlanifiedRendezVousByMedecin(Integer medecinId) {
        return rendezvousRepository.findPlanifiedByMedecin(medecinId)
                .stream()
                .map(RendezvousDto::fromEntity)
                .toList();
    }

    @Override
    public List<RendezvousDto> getCancelledRendezVousByMedecin(Integer medecinId) {
        return rendezvousRepository.findCancelledByMedecin(medecinId)
                .stream()
                .map(RendezvousDto::fromEntity)
                .toList();
    }

    private void checkPatientAvailability(Integer patientId, LocalDate date)  {
        /*
        * [00:00:00,23:59:59]
        * */

        //transformer une date (sans heure) en un datetime fixé à 00:00:00)
        LocalDateTime startOfDay = date.atStartOfDay();
        //ajoute 1 jour,enlève 1 nanoseconde
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        if (rendezvousRepository.existsByPatientAndDate(patientId, startOfDay, endOfDay)) {
            throw new ConflictException("Le patient a déjà un rendez-vous ce jour-là");
        }
    }
}
