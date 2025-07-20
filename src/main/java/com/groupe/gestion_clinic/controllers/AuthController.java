package com.groupe.gestion_clinic.controllers;


import com.groupe.gestion_clinic.dto.AuthResponse;
import com.groupe.gestion_clinic.dto.requestDto.LoginRequestDto;
import com.groupe.gestion_clinic.model.Utilisateur;
import com.groupe.gestion_clinic.repositories.UtilisateurRepository;
import com.groupe.gestion_clinic.security.JwtServiceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthenticationManager authenticationManager;
    private final JwtServiceUtil jwtServiceUtil;
    private final UtilisateurRepository utilisateurRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request){
        try {
            // Tente d'authentifier l'utilisateur avec l'email et le mot de passe
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            // Si l'authentification réussit, met à jour le contexte de sécurité
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Récupère l'objet Utilisateur complet pour générer le token avec les claims nécessaires
            Utilisateur user = utilisateurRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé après authentification."));

            // Génère le token JWT
            String token = jwtServiceUtil.generateToken(user);

            // Retourne le token au client
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException e) {
            // Gère les échecs d'authentification (ex: mauvais identifiants)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants invalides: " + e.getMessage());
        }
    }

}
