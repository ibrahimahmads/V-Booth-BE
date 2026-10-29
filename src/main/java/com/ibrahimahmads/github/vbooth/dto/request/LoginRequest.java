package com.ibrahimahmads.github.vbooth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email atau username tidak boleh kosong!!!")
        String usernameOrEmail,
        @NotBlank(message = "Password tidak boleh kosong!!!")
        String password
) {
}
