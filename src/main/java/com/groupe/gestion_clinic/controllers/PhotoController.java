package com.groupe.gestion_clinic.controllers;

import com.groupe.gestion_clinic.services.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping("/upload/{userType}/{userId}")
    @PreAuthorize("hasAnyRole('MEDECIN', 'SECRETAIRE', 'ADMIN')")
    public ResponseEntity<String> uploadPhoto(
            @PathVariable String userType,
            @PathVariable Integer userId,
            @RequestParam("file") MultipartFile file) {
        
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Fichier vide");
        }

        if (!userType.equals("medecin") && !userType.equals("secretaire")) {
            return ResponseEntity.badRequest().body("Type d'utilisateur invalide");
        }

        String fileName = photoService.uploadPhoto(file, userType, userId);
        return ResponseEntity.ok("Photo uploadée avec succès: " + fileName);
    }

    @DeleteMapping("/{userType}/{userId}")
    @PreAuthorize("hasAnyRole('MEDECIN', 'SECRETAIRE', 'ADMIN')")
    public ResponseEntity<String> deletePhoto(
            @PathVariable String userType,
            @PathVariable Integer userId) {
        
        if (!userType.equals("medecin") && !userType.equals("secretaire")) {
            return ResponseEntity.badRequest().body("Type d'utilisateur invalide");
        }

        photoService.deletePhoto(userType, userId);
        return ResponseEntity.ok("Photo supprimée avec succès");
    }

    @GetMapping("/{userType}/{userId}")
    public ResponseEntity<byte[]> getPhoto(
            @PathVariable String userType,
            @PathVariable Integer userId) {
        
        if (!userType.equals("medecin") && !userType.equals("secretaire")) {
            return ResponseEntity.badRequest().build();
        }

        byte[] photoData = photoService.getPhoto(userType, userId);
        
        if (photoData == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .body(photoData);
    }
}