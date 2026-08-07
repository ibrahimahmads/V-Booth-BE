package com.ibrahimahmads.github.vbooth.controller;

import com.ibrahimahmads.github.vbooth.dto.request.GreetingRequest;
import com.ibrahimahmads.github.vbooth.dto.response.DownloadPhotoResponse;
import com.ibrahimahmads.github.vbooth.dto.response.GreetingsResponse;
import com.ibrahimahmads.github.vbooth.service.GreetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/greetings")
@RequiredArgsConstructor
public class GreetingController {
    private final GreetingService greetingService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GreetingsResponse> save(@ModelAttribute GreetingRequest payload) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(greetingService.save(payload));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<GreetingsResponse> showGreetingById(@PathVariable UUID id) {
        return ResponseEntity.ok(greetingService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<GreetingsResponse>> showAllGreetings() {
        return ResponseEntity.ok(greetingService.getAll());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GreetingsResponse> update(
            @PathVariable UUID id,
            @ModelAttribute GreetingRequest payload) {

        GreetingsResponse response = greetingService.update(id, payload);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        greetingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<InputStreamResource> downloadHDPhoto(@PathVariable UUID id) {
        try {
            DownloadPhotoResponse response = greetingService.downloadPhoto(id);
            return ResponseEntity.ok()
                    .headers(response.getHeaders())
                    .contentType(MediaType.IMAGE_PNG)
                    .body(new InputStreamResource(response.getInputStream()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
