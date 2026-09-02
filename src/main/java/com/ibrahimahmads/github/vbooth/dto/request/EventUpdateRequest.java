package com.ibrahimahmads.github.vbooth.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibrahimahmads.github.vbooth.utils.validations.ValidSlug;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventUpdateRequest(
        @NotBlank(message = "Nama event wajib diisi")
        String eventName,

        @NotBlank(message = "Slug wajib diisi")
        @Size(min = 3, max = 60)
        @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$")
        @ValidSlug
        String slug,

        String eventDescription,

        @NotNull(message = "Tanggal event wajib diisi")
        @FutureOrPresent(message = "Tanggal event tidak boleh tanggal yang sudah lewat")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate eventDate,

        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime eventStartTime,

        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime eventEndTime,

        String eventLocation
) {}
