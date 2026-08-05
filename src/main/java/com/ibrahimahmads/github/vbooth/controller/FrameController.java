package com.ibrahimahmads.github.vbooth.controller;

import com.ibrahimahmads.github.vbooth.dto.request.FrameRequest;
import com.ibrahimahmads.github.vbooth.entity.Frame;
import com.ibrahimahmads.github.vbooth.service.FrameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/frames")
public class FrameController {
    private final FrameService frameService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Frame> save(@ModelAttribute FrameRequest payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(frameService.saveFrame(payload));
    }

    @GetMapping
    public ResponseEntity<List<Frame>> getAllFrames() {
        return ResponseEntity.ok(frameService.getAllFrames());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Frame> update(@ModelAttribute FrameRequest payload, @PathVariable UUID id) {
        return ResponseEntity.ok(frameService.updateFrame(id, payload));
    }
}
