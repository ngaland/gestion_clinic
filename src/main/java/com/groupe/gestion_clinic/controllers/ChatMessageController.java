package com.groupe.gestion_clinic.controllers;


import com.groupe.gestion_clinic.dto.ChatMessageDto;
import com.groupe.gestion_clinic.dto.requestDto.ChatMessageRequestDto;
import com.groupe.gestion_clinic.services.ChatMesssageService;
import com.groupe.gestion_clinic.services.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/*
* Utilisez @Controller pour les contrôleurs STOMP
* @RestController Nécessaire pour des endpoints REST
* */
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@RestController
public class ChatMessageController {

    private final ChatMesssageService chatMessageService;

    // Pour récupérer l'ID de l'utilisateur authentifié
    private final UtilisateurService utilisateurService;

    /*
    * Endpoint pour envoyer un message (via WebSocket)
    * Les messages seront envoyés à /app/chat.sendMessage (préfixe /app défini dans WebSocketConfig)
    * */
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageRequestDto chatMessageRequest,
                            @AuthenticationPrincipal UserDetails userDetails) {
        // L'ID de l'expéditeur est extrait de l'utilisateur authentifié
        Integer senderId = utilisateurService.getUtilisateurByEmail(userDetails.getUsername()).getId();
        chatMessageService.saveAndSendMessage(chatMessageRequest, senderId);
    }

    // Endpoint pour marquer les messages comme lus (via WebSocket)
    @MessageMapping("/chat.markAsRead")
    public void markAsRead(@Payload Map<String, Integer> payload,
                           @AuthenticationPrincipal UserDetails userDetails) {
        Integer senderToMarkRead = payload.get("senderId");
        Integer currentUserId = utilisateurService.getUtilisateurByEmail(userDetails.getUsername()).getId();
        if (senderToMarkRead != null) {
            chatMessageService.markMessagesAsRead(senderToMarkRead, currentUserId);
        }
    }

    /*
    * Endpoint REST pour récupérer l'historique d'une conversation (pour l'initialisation du chat)
    * */
    @GetMapping("/conversation/{otherUserId}")
    public ResponseEntity<List<ChatMessageDto>> getConversation(@PathVariable Integer otherUserId,
                                                                @AuthenticationPrincipal UserDetails userDetails) {
        Integer currentUserId = utilisateurService.getUtilisateurByEmail(userDetails.getUsername()).getId();
        List<ChatMessageDto> conversation = chatMessageService.getConversation(currentUserId, otherUserId);
        return ResponseEntity.ok(conversation);
    }


    // Endpoint REST pour récupérer les messages non lus de l'utilisateur actuel
    @GetMapping("/unread")
    public ResponseEntity<List<ChatMessageDto>> getUnreadMessages(@AuthenticationPrincipal UserDetails userDetails) {
        Integer currentUserId = utilisateurService.getUtilisateurByEmail(userDetails.getUsername()).getId();
        List<ChatMessageDto> unreadMessages = chatMessageService.getUnreadMessagesForUser(currentUserId);
        return ResponseEntity.ok(unreadMessages);
    }

    // Endpoint REST pour récupérer le nombre de messages non lus par expéditeur
    @GetMapping("/unread/counts")
    public ResponseEntity<Map<Integer, Long>> getUnreadMessageCounts(@AuthenticationPrincipal UserDetails userDetails) {
        Integer currentUserId = utilisateurService.getUtilisateurByEmail(userDetails.getUsername()).getId();
        Map<Integer, Long> counts = chatMessageService.getUnreadMessageCountsBySender(currentUserId);
        return ResponseEntity.ok(counts);
    }



}
