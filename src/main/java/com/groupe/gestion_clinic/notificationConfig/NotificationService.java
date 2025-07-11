package com.groupe.gestion_clinic.notificationConfig;


import com.groupe.gestion_clinic.dto.NotificationDto;
import com.groupe.gestion_clinic.model.Rendezvous;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


/*
* ----------------------les composants  cles --------------------------
*
* WebSocket : Connexion persistante pour les communications bidirectionnelles
* STOMP : Protocole de messagerie sur WebSocket (comme HTTP pour WS)
* SockJS : Fallback pour les navigateurs ne supportant pas WS
* SimpMessagingTemplate : Outil Spring pour envoyer des messages via WS
*
* --------------- flux de fonctionement ------------------------
* Le client (navigateur) établit une connexion WebSocket
* Le client s'abonne à des canaux de notification (/topic/..., /user/queue/...)
* Le serveur envoie des notifications via ces canaux lorsque des événements se produisen
* Le client reçoit et affiche les notifications en temps réel
*
* */

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final EmailService emailService;



    // Notification privée à un utilisateur
    public void sendPrivateNotification(Long userId, NotificationDto notification) {
        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/privateNotifications",
                notification
        );
    }

    // Notification publique (pour tous les abonnés)
    public void sendPublicNotification(NotificationDto notification) {
        messagingTemplate.convertAndSend("/topic/publicNotifications", notification);
    }


    // Envoi de rappel de RDV
    public void sendRappelRendezVous(Rendezvous rdv) {
        // Notification UI
        NotificationDto notif = new NotificationDto(
                "RDV_REMINDER",
                "Rappel: RDV avec " + rdv.getPatient().getNom() + " à " + rdv.getDateHeureDebut().toLocalTime(),
                rdv.getId(),
                LocalDateTime.now(),
                "MEDECIN",
                rdv.getMedecin().getId().longValue()
        );

        sendPrivateNotification(rdv.getMedecin().getId().longValue(), notif);

        // Email au médecin
        emailService.sendReminderEmail(
                                        rdv.getMedecin().getEmail(),
                                        "Rappel de rendez-vous",
                                        "Rappel: RDV avec " +
                                                rdv.getPatient().getNom() + " à " +
                                                rdv.getDateHeureDebut().toLocalTime()

        );

        // Notification + email au patient
        NotificationDto patientNotif = new NotificationDto(
                    "RDV_REMINDER",
                    "Rappel: RDV avec "+rdv.getMedecin().getNom()+rdv.getDateHeureDebut().toLocalTime(),
                    rdv.getId(), LocalDateTime.now(),"PATIENT",rdv.getMedecin().getId().longValue()
                );

        sendPrivateNotification(rdv.getPatient().getId().longValue(), patientNotif);
        emailService.sendReminderEmail(
                                        rdv.getMedecin().getEmail(),
                                        "Rappel de rendez-vous",
                                        "Rappel : RDV avec "+rdv.getMedecin().getNom()+" a "+rdv.getDateHeureDebut().toLocalTime());
    }


}



