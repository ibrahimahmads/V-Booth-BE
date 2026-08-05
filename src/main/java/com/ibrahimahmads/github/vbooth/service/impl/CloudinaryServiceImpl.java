package com.ibrahimahmads.github.vbooth.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ibrahimahmads.github.vbooth.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;
    @Override
    public String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File gambar tidak boleh kosong");
        }

        if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")){
            throw new IllegalArgumentException("File harus berupa gambar!");
        }
        try{
            Map result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "grettings-photos",
                            "public_id", UUID.randomUUID().toString()
                    )
            );
            return result.get("secure_url").toString();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String uploadImageFrame(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File gambar tidak boleh kosong");
        }

        if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")){
            throw new IllegalArgumentException("File harus berupa gambar!");
        }
        try{
            Map result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "overlay-photos",
                            "public_id", UUID.randomUUID().toString()
                    )
            );
            return result.get("secure_url").toString();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String uploadAudio(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File audio tidak boleh kosong");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("audio/")) {
            throw new IllegalArgumentException("File harus berupa audio!");
        }

        try {
            Map result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "greetings-audios",
                            "public_id", UUID.randomUUID().toString(),
                            "resource_type", "video"
                    )
            );
            return result.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Gagal mengunggah audio ke Cloudinary: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteFile(String fileUrl, String resourceType) {
        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }

        try {
            String publicId = extractPublicIdFromUrl(fileUrl);

            if (publicId != null) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.asMap(
                        "resource_type", resourceType
                ));
            }
        } catch (Exception e) {
            System.err.println("Gagal menghapus file lama dari Cloudinary: " + e.getMessage());
        }
    }

    private String extractPublicIdFromUrl(String url) {
        try {
            int uploadIndex = url.indexOf("/upload/");
            if (uploadIndex == -1) return null;
            
            String pathAfterUpload = url.substring(uploadIndex + 8);

            if (pathAfterUpload.startsWith("v")) {
                int firstSlash = pathAfterUpload.indexOf("/");
                pathAfterUpload = pathAfterUpload.substring(firstSlash + 1);
            }

            int lastDot = pathAfterUpload.lastIndexOf(".");
            if (lastDot != -1) {
                pathAfterUpload = pathAfterUpload.substring(0, lastDot);
            }

            return pathAfterUpload;
        } catch (Exception e) {
            return null;
        }
    }
}
