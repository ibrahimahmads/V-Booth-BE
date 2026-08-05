package com.ibrahimahmads.github.vbooth.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record FrameRequest(
        String nameTheme,
        MultipartFile overlay
) {
}
