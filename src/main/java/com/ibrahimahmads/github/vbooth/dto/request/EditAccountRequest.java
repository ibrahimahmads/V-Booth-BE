package com.ibrahimahmads.github.vbooth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EditAccountRequest(
        @NotBlank(message = "Nama tidak boleh kosong!!!")
        String name,
        @NotBlank(message = "Username tidak boleh kosong!!!")
        @Size(min = 3, max = 20, message = "Panjang username harus antara 3 dan 20 karakter")
        String username,
        @NotBlank(message = "Email tidak boleh kosong!!!")
        @Email(message = "Format email tidak valid")
        @Size(max = 100, message = "Panjang email maksimal 100 karakter")
        String email
) {
}
