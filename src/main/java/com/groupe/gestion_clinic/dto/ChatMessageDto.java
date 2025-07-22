package com.groupe.gestion_clinic.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {
    private String senderEmail;
    private String receiverEmail;
    private String content;
}
