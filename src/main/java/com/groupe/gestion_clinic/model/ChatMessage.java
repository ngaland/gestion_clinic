package com.groupe.gestion_clinic.model;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@Getter @Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage extends AbstractEntity{

    private String senderEmail;
    private String receiverEmail;
    private String content;
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private ChatMessageStatut chatStatus;

    private Boolean isRead;
}
