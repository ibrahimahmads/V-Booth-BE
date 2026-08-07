package com.ibrahimahmads.github.vbooth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;

import java.io.InputStream;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DownloadPhotoResponse {
    private InputStream inputStream;
    private HttpHeaders headers;
}
