package com.ibrahimahmads.github.vbooth.service.impl;

import com.ibrahimahmads.github.vbooth.dto.request.PaketRequest;
import com.ibrahimahmads.github.vbooth.entity.Paket;
import com.ibrahimahmads.github.vbooth.repository.PaketRepository;
import com.ibrahimahmads.github.vbooth.service.PaketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaketServiceImpl implements PaketService {
    private final PaketRepository paketRepository;
    @Override
    public Paket save(PaketRequest payload) {
        Paket newPaket = Paket.builder()
                .name(payload.name())
                .price(payload.price())
                .description(payload.description())
                .maxGreetings(payload.maxGreetings())
                .eventActivePeriod(payload.eventActivePeriod())
                .galeryActivePeriod(payload.galeryActivePeriod())
                .isCanPhotoAndVoice(true)
                .isUrlSlug(true)
                .build();
        return paketRepository.save(newPaket);
    }

    @Override
    public Paket getById(UUID id) {
        return paketRepository.findById(id).orElseThrow(()-> new RuntimeException("Paket with id: "+id+" not found"));
    }

    @Override
    public Paket edit(UUID id, PaketRequest payload) {
        Paket paket = getById(id);
        paket.setDescription(payload.description());
        paket.setPrice(payload.price());
        paket.setName(payload.name());
        paket.setEventActivePeriod(payload.eventActivePeriod());
        paket.setMaxGreetings(payload.maxGreetings());
        paket.setGaleryActivePeriod(payload.galeryActivePeriod());
        return paketRepository.save(paket);
    }

    @Override
    public void delete(UUID id) {
        Paket paket = getById(id);
        paketRepository.delete(paket);
    }

    @Override
    public List<Paket> getAll() {
        return paketRepository.findAll();
    }
}
