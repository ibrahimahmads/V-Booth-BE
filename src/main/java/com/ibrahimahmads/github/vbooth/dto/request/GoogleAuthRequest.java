package com.ibrahimahmads.github.vbooth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GoogleAuthRequest(
        @NotBlank(message = "Google id token tidak boleh kosong!!!")
        String googleIdToken
) {
}
