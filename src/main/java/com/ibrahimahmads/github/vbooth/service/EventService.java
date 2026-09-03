package com.ibrahimahmads.github.vbooth.service;

import com.ibrahimahmads.github.vbooth.dto.request.EventCreateRequest;
import com.ibrahimahmads.github.vbooth.dto.request.EventUpdateRequest;
import com.ibrahimahmads.github.vbooth.dto.response.EventResponse;

import java.util.List;
import java.util.UUID;

public interface EventService {
    EventResponse save(UUID accountId,EventCreateRequest payload);
    EventResponse getEventMeById(UUID id, UUID accountId);
    EventResponse getEventById(UUID id);
    EventResponse update(UUID accountId,UUID id, EventUpdateRequest payload);
    void delete(UUID id, UUID accountId);
    List<EventResponse> getAll();
    List<EventResponse> getAllByAccountId(UUID accountId);
}
