package com.groupe.gestion_clinic.services.serviceImpl;


import com.groupe.gestion_clinic.exceptions.UserNotFoundException;
import com.groupe.gestion_clinic.model.Utilisateur;
import com.groupe.gestion_clinic.repositories.UtilisateurRepository;
import com.groupe.gestion_clinic.services.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    @Override
    public Utilisateur getUtilisateurByEmail(String email) {
        Utilisateur theUser;
         theUser = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'email: " + email));

        return theUser;
    }
}
