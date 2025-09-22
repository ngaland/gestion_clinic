package com.groupe.gestion_clinic.services;

import org.springframework.web.multipart.MultipartFile;

public interface PhotoService {
    String uploadPhoto(MultipartFile file, String userType, Integer userId);
    void deletePhoto(String userType, Integer userId);
    byte[] getPhoto(String userType, Integer userId);
}