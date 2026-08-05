package com.ibrahimahmads.github.vbooth.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record GreetingRequest(
    String guestName,
    MultipartFile photo,
    MultipartFile audio
) {
}
