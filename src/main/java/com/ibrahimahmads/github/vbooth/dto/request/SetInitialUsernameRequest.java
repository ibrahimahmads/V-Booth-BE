package com.ibrahimahmads.github.vbooth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SetInitialUsernameRequest(
        @NotBlank(message = "Username tidak boleh kosong!!!")
        @Size(min = 3, max = 20, message = "Panjang username harus antara 3 dan 20 karakter")
        String username
) {
}
