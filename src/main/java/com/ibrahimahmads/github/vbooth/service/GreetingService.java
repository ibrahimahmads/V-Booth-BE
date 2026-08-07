package com.ibrahimahmads.github.vbooth.service;

import com.ibrahimahmads.github.vbooth.dto.request.GreetingRequest;
import com.ibrahimahmads.github.vbooth.dto.response.DownloadPhotoResponse;
import com.ibrahimahmads.github.vbooth.dto.response.GreetingsResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface GreetingService {
    GreetingsResponse save(GreetingRequest greetings);
    GreetingsResponse getById(UUID id);
    GreetingsResponse update(UUID id, GreetingRequest greetings);
    List<GreetingsResponse> getAll();
    void deleteById(UUID id);
    DownloadPhotoResponse downloadPhoto(UUID id) throws IOException;
}
