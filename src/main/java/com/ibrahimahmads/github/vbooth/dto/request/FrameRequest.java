package com.ibrahimahmads.github.vbooth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record FrameRequest(
        @NotBlank(message = "Tema tidak boleh kosong!!!")
        String nameTheme,
        @NotNull(message = "Overlay tidak boleh kosong!!!")
        MultipartFile overlay
) {
}
