package com.groupe.gestion_clinic.services.serviceImpl;

import com.groupe.gestion_clinic.services.PhotoService;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name:clinic-photos}")
    private String bucketName;

    @Override
    public String uploadPhoto(MultipartFile file, String userType, Integer userId) {
        try {
            // Créer le bucket s'il n'existe pas
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // Nom du fichier: userType/userId.extension
            String fileName = userType + "/" + userId + getFileExtension(file.getOriginalFilename());

            // Upload du fichier
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'upload de la photo: " + e.getMessage());
        }
    }

    @Override
    public void deletePhoto(String userType, Integer userId) {
        try {
            String fileName = userType + "/" + userId;
            
            // Chercher et supprimer tous les fichiers commençant par ce nom
            Iterable<Result<Item>> objects = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(fileName)
                            .build()
            );

            for (Result<Item> result : objects) {
                Item item = result.get();
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(bucketName)
                                .object(item.objectName())
                                .build()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de la photo: " + e.getMessage());
        }
    }

    @Override
    public byte[] getPhoto(String userType, Integer userId) {
        try {
            String fileName = userType + "/" + userId;
            
            // Chercher le fichier
            Iterable<Result<Item>> objects = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(fileName)
                            .build()
            );

            for (Result<Item> result : objects) {
                Item item = result.get();
                try (InputStream stream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(bucketName)
                                .object(item.objectName())
                                .build()
                )) {
                    return stream.readAllBytes();
                }
            }
            
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de la photo: " + e.getMessage());
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return ".jpg";
    }
}