package com.ibrahimahmads.github.vbooth.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadImage(MultipartFile file);
    String uploadImageFrame(MultipartFile file);
    String uploadAudio(MultipartFile file);
    void deleteFile(String fileUrl, String resourceType);
}
