package com.ibrahimahmads.github.vbooth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record GreetingRequest(
        @NotBlank(message = "Nama tamu tidak boleh kosong!!!")
        String guestName,
        @NotNull(message = "Foto tidak boleh kosong!!!")
        MultipartFile photo,
        @NotNull(message = "Audio tidak boleh kosong!!!")
        MultipartFile audio
) {
}
