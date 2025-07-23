package com.groupe.gestion_clinic.services;

import com.groupe.gestion_clinic.model.Utilisateur;


public interface UtisateurService {
    public Utilisateur getUtilisateurByEmail(String email);
}
