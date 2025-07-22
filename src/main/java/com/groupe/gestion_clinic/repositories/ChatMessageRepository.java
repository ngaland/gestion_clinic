package com.groupe.gestion_clinic.repositories;

import com.groupe.gestion_clinic.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Integer> {

}
