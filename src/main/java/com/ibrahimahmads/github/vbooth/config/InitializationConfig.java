package com.ibrahimahmads.github.vbooth.config;

import com.ibrahimahmads.github.vbooth.entity.Account;
import com.ibrahimahmads.github.vbooth.entity.Paket;
import com.ibrahimahmads.github.vbooth.repository.PaketRepository;
import com.ibrahimahmads.github.vbooth.service.AccountService;
import com.ibrahimahmads.github.vbooth.service.PaketService;
import com.ibrahimahmads.github.vbooth.utils.enums.Roles;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

@Component
@RequiredArgsConstructor
public class InitializationConfig {
    private final PaketRepository paketRepository;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @Value("${vbooth.admin.password}")
    private String passAdmin;
    @Value("${vbooth.admin.email}")
    private String emailAdmin;
    @Value("${vbooth.admin.username}")
    private String usernameAdmin;

    @PostConstruct
    public void init(){
        initTimezone();
        initPaket();
        initAdmin();
    }

    void initTimezone(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Jakarta"));
    }

    void initPaket(){
        if(!paketRepository.existsByName("Intimate")){
            Paket paket = Paket.builder()
                    .name("Intimate")
                    .price(150000L)
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
                    .price(300000L)
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
                    .price(450000L)
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

    void initAdmin(){
        if(!accountService.isUsernameExist(usernameAdmin)){
            Account account = Account.builder()
                    .name(StringUtils.capitalize(usernameAdmin))
                    .username(usernameAdmin)
                    .role(Roles.ADMIN)
                    .isEmailVerified(true)
                    .password(passwordEncoder.encode(passAdmin))
                    .email(emailAdmin)
                    .build();
            accountService.save(account);
        }
    }
}
