package com.ibrahimahmads.github.vbooth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank(message = "Password lama tidak boleh kosong!!!")
        String oldPassword,
        @NotBlank(message = "Password baru tidak boleh kosong!!!")
        @Size(min = 8, message = "Password harus memiliki panjang minimal 8 karakter")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).*$",
                message = "Password harus memiliki setidaknya 1 huruf kapital, 1 angka dan 1 karakter khusus"
        )
        String newPassword
) {
}
