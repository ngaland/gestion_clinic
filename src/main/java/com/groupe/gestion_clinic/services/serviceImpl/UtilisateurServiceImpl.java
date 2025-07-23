package com.groupe.gestion_clinic.services.serviceImpl;


import com.groupe.gestion_clinic.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UtilisateurServiceImpl {

    private UtilisateurRepository utilisateurRepository;
}
