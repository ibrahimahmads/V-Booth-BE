package com.ibrahimahmads.github.vbooth.service.impl;

import com.ibrahimahmads.github.vbooth.dto.request.FrameRequest;
import com.ibrahimahmads.github.vbooth.entity.Frame;
import com.ibrahimahmads.github.vbooth.repository.FrameRepository;
import com.ibrahimahmads.github.vbooth.service.CloudinaryService;
import com.ibrahimahmads.github.vbooth.service.FrameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FrameServiceImpl implements FrameService {
    private final FrameRepository frameRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public Frame saveFrame(FrameRequest payload) {
        if (payload.nameTheme() == null || payload.nameTheme().isBlank()) {
            throw new IllegalArgumentException("Nama tema tidak boleh kosong");
        }
        String overlay = cloudinaryService.uploadImageFrame(payload.overlay());
        Frame frame = Frame.builder()
                .nameTheme(payload.nameTheme())
                .overlayUrl(overlay)
                .build();
        return frameRepository.save(frame);
    }

    @Override
    public List<Frame> getAllFrames() {
        return frameRepository.findAll();
    }

    @Override
    public Frame updateFrame(UUID id, FrameRequest payload) {
        if (payload.nameTheme().isBlank()) {
            throw new IllegalArgumentException("Nama tema tidak boleh kosong");
        }

        if (payload.overlay().isEmpty()) {
            throw new IllegalArgumentException("Overlay tidak boleh kosong");
        }

        Frame frame = frameRepository.findById(id).orElseThrow();
        cloudinaryService.deleteFile(frame.getOverlayUrl(), "image");
        String overlay = cloudinaryService.uploadImageFrame(payload.overlay());
        frame.setNameTheme(payload.nameTheme());
        frame.setOverlayUrl(overlay);
        return frameRepository.save(frame);

    }

    @Override
    public void deleteFrame(UUID id) {
        Frame frame = frameRepository.findById(id).orElseThrow();
        cloudinaryService.deleteFile(frame.getOverlayUrl(), "image");
        frameRepository.delete(frame);
        System.out.println("Frame deleted: " + id);
    }
}
