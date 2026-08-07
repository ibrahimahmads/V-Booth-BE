package com.ibrahimahmads.github.vbooth.service.impl;

import com.ibrahimahmads.github.vbooth.dto.request.GreetingRequest;
import com.ibrahimahmads.github.vbooth.dto.response.DownloadPhotoResponse;
import com.ibrahimahmads.github.vbooth.dto.response.GreetingsResponse;
import com.ibrahimahmads.github.vbooth.entity.Greetings;
import com.ibrahimahmads.github.vbooth.repository.GreetingRepository;
import com.ibrahimahmads.github.vbooth.service.CloudinaryService;
import com.ibrahimahmads.github.vbooth.service.GreetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GreetingServiceImpl implements GreetingService {
    private final GreetingRepository greetingRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public GreetingsResponse save(GreetingRequest payload) {
        if (payload.guestName() == null || payload.guestName().isBlank()) {
            throw new IllegalArgumentException("Nama tamu tidak boleh kosong");
        }
        String photoUrl = cloudinaryService.uploadImage(payload.photo());
        String audioUrl = cloudinaryService.uploadAudio(payload.audio());
        Greetings greeting = Greetings.builder()
                .guestName(payload.guestName())
                .photoUrl(photoUrl)
                .audioUrl(audioUrl)
                .createdAt(LocalDateTime.now())
                .build();
        return greetingRepository.save(greeting).toResponse();
    }

    @Override
    public GreetingsResponse getById(UUID id) {
        return greetingRepository.findByid(id).toResponse();
    }

    @Override
    @Transactional
    public GreetingsResponse update(UUID id,GreetingRequest payload) {
        Greetings greeting = greetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Greeting tidak ditemukan dengan ID: " + id));

        if (payload.guestName() != null && !payload.guestName().isBlank()) {
            greeting.setGuestName(payload.guestName());
        }

        if (payload.photo() != null && !payload.photo().isEmpty()) {
            cloudinaryService.deleteFile(greeting.getPhotoUrl(), "image");
            String newPhotoUrl = cloudinaryService.uploadImage(payload.photo());
            greeting.setPhotoUrl(newPhotoUrl);
        }

        if (payload.audio() != null && !payload.audio().isEmpty()) {
            cloudinaryService.deleteFile(greeting.getAudioUrl(), "video");
            String newAudioUrl = cloudinaryService.uploadAudio(payload.audio());
            greeting.setAudioUrl(newAudioUrl);
        }

        Greetings updatedGreeting = greetingRepository.save(greeting);
        return updatedGreeting.toResponse();
    }

    @Override
    public List<GreetingsResponse> getAll() {
        return greetingRepository.findAll().stream().map(Greetings::toResponse).toList();
    }

    @Override
    public void deleteById(UUID id) {
        Greetings greetings = greetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Greeting tidak ditemukan dengan ID: " + id));
        cloudinaryService.deleteFile(greetings.getPhotoUrl(), "image");
        cloudinaryService.deleteFile(greetings.getAudioUrl(), "video");
        greetingRepository.delete(greetings);
    }

    @Override
    public DownloadPhotoResponse downloadPhoto(UUID id) throws IOException {
        GreetingsResponse greeting = getById(id);
        String hdCloudinaryUrl = greeting.getPhotoUrl().replace("/upload/", "/upload/q_100,f_png/");
        URL url = URI.create(hdCloudinaryUrl).toURL();
        InputStream inputStream = url.openStream();
        String fileName = "vbooth-greeting-" + greeting.getGuestName() + ".png";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
        return DownloadPhotoResponse.builder()
                .inputStream(inputStream)
                .headers(headers)
                .build();
    }
}
