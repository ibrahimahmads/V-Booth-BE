package com.ibrahimahmads.github.vbooth.service;

import com.ibrahimahmads.github.vbooth.dto.request.EventCreateRequest;
import com.ibrahimahmads.github.vbooth.dto.request.EventUpdateRequest;
import com.ibrahimahmads.github.vbooth.dto.response.EventResponse;

import java.util.UUID;

public interface EventService {
    EventResponse save(EventCreateRequest payload);
    EventResponse getEventById(UUID id);
    EventResponse update(UUID id, EventUpdateRequest payload);
    void delete(UUID id);
}
