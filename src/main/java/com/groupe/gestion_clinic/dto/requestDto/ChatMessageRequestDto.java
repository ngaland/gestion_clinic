package com.groupe.gestion_clinic.dto.requestDto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequestDto {
    // L'ID de l'expéditeur sera extrait du contexte de sécurité (token JWT)
    private Integer receiverId;
    private String content;


}
