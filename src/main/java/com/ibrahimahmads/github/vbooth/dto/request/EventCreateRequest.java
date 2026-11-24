package com.ibrahimahmads.github.vbooth.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibrahimahmads.github.vbooth.utils.validations.ValidSlug;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventCreateRequest(
        @NotBlank(message = "Nama event wajib diisi")
        String eventName,
        @NotBlank(message = "Slug wajib diisi")
        @Size(min = 3, max = 60, message = "Panjang slug minimal 3 dan maksimal 60 karakter")
        @Pattern(
                regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
                message = "Slug hanya boleh berisi huruf kecil, angka, dan tanda hubung (-)"
        )
        @ValidSlug
        String slug,
        @NotBlank(message = "Deskripsi event wajib diisi")
        String eventDescription,
        @NotNull(message = "Tanggal event wajib diisi")
        @FutureOrPresent(message = "Tanggal event tidak boleh tanggal yang sudah lewat")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate eventDate,
        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime eventStartTime,
        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime eventEndTime,
        @NotBlank(message = "Lokasi event wajib diisi")
        String eventLocation
) {
}
