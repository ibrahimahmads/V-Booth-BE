package com.ibrahimahmads.github.vbooth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email tidak boleh kosong!!!")
        String email,
        @NotBlank(message = "Password tidak boleh kosong!!!")
        String password
) {
}
