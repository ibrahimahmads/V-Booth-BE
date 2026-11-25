package com.ibrahimahmads.github.vbooth.service;

import com.ibrahimahmads.github.vbooth.dto.request.EventCreateRequest;
import com.ibrahimahmads.github.vbooth.dto.request.EventUpdateRequest;
import com.ibrahimahmads.github.vbooth.dto.request.EventUserSeach;
import com.ibrahimahmads.github.vbooth.dto.response.EventResponse;
import com.ibrahimahmads.github.vbooth.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EventService {
    EventResponse save(UUID accountId,EventCreateRequest payload);
    EventResponse getEventMeById(UUID id, UUID accountId);
    EventResponse getEventById(UUID id);
    EventResponse update(UUID accountId,UUID id, EventUpdateRequest payload);
    void delete(UUID id, UUID accountId);
    Page<EventResponse> getAll(Pageable pageable, EventUserSeach search);
    Page<EventResponse> getAllByAccountId(UUID accountId, Pageable pageable, EventUserSeach search);
    EventResponse save(Event event);
}
