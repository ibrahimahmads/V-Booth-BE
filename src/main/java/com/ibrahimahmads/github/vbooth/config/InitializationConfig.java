package com.ibrahimahmads.github.vbooth.config;

import com.ibrahimahmads.github.vbooth.entity.Paket;
import com.ibrahimahmads.github.vbooth.repository.PaketRepository;
import com.ibrahimahmads.github.vbooth.service.PaketService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

@Component
@RequiredArgsConstructor
public class InitializationConfig {
    private final PaketRepository paketRepository;
    @PostConstruct
    public void init(){
        initTimezone();
        initPaket();
    }

    void initTimezone(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Jakarta"));
    }

    void initPaket(){
        if(!paketRepository.existsByName("Intimate")){
            Paket paket = Paket.builder()
                    .name("Intimate")
                    .price(150000)
                    .description("Cocok untuk acara intim")
                    .maxGuest(150)
                    .eventActivePeriod(1)
                    .galeryActivePeriod(7)
                    .isCanPhotoAndVoice(true)
                    .isUrlSlug(true)
                    .build();
            paketRepository.save(paket);
        }
        if(!paketRepository.existsByName("Celebration")){
            Paket paket = Paket.builder()
                    .name("Celebration")
                    .price(300000)
                    .description("Untuk resepsi & acara besar")
                    .maxGuest(300)
                    .eventActivePeriod(1)
                    .galeryActivePeriod(14)
                    .isCanPhotoAndVoice(true)
                    .isUrlSlug(true)
                    .build();
            paketRepository.save(paket);
        }
        if(!paketRepository.existsByName("Enterprise")){
            Paket paket = Paket.builder()
                    .name("Enterprise")
                    .price(450000)
                    .description("Untuk gathering & acara perusahaan")
                    .maxGuest(500)
                    .eventActivePeriod(1)
                    .galeryActivePeriod(14)
                    .isCanPhotoAndVoice(true)
                    .isUrlSlug(true)
                    .build();
            paketRepository.save(paket);
        }
    }
}
