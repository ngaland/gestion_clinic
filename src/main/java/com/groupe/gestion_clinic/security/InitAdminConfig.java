package com.groupe.gestion_clinic.security;


import com.groupe.gestion_clinic.model.Administrateur;
import com.groupe.gestion_clinic.model.Role;
import com.groupe.gestion_clinic.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class InitAdminConfig {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initAdmin() {
        return args -> {
            if (utilisateurRepository.count() == 0) {
                Administrateur admin = new Administrateur();
                admin.setNom("Super");
                admin.setPrenom("Admin");
                admin.setEmail("admin@example.com");
                admin.setMotDePasse(passwordEncoder.encode("admin123")); // password hashé
                admin.setRole(Role.ADMIN);

                utilisateurRepository.save(admin);
                System.out.println("✅ Admin créé avec succès: email=admin@example.com, password=admin123");
            }
        };
    }
}
