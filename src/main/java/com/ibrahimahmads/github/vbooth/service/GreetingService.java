package com.ibrahimahmads.github.vbooth.service;

import com.ibrahimahmads.github.vbooth.dto.request.GreetingRequest;
import com.ibrahimahmads.github.vbooth.dto.response.GreetingsResponse;
import com.ibrahimahmads.github.vbooth.entity.Greetings;

import java.util.List;
import java.util.UUID;

public interface GreetingService {
    Greetings save(GreetingRequest greetings);
    GreetingsResponse getById(UUID id);
    GreetingsResponse update(UUID id, GreetingRequest greetings);
    List<GreetingsResponse> getAll();
    void deleteById(UUID id);
}
