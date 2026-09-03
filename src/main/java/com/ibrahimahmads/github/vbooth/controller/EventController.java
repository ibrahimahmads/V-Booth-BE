package com.ibrahimahmads.github.vbooth.controller;

import com.ibrahimahmads.github.vbooth.dto.request.EventUpdateRequest;
import com.ibrahimahmads.github.vbooth.dto.response.EventResponse;
import com.ibrahimahmads.github.vbooth.security.model.UserContext;
import com.ibrahimahmads.github.vbooth.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/me/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EventResponse> getMyEventDetail(Authentication authentication, @PathVariable UUID id) {
        if(authentication.getPrincipal() == null ) throw new NullPointerException("login first to access this resource");
        UserContext userContext = (UserContext) authentication.getPrincipal();
        return ResponseEntity.ok(eventService.getEventMeById(id,userContext.getAccountId()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> getEventDetail(@PathVariable UUID id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<EventResponse>> getAllMyEvent(Authentication authentication) {
        if(authentication.getPrincipal() == null ) throw new NullPointerException("login first to access this resource");
        UserContext userContext = (UserContext) authentication.getPrincipal();
        return ResponseEntity.ok(eventService.getAllByAccountId(userContext.getAccountId()));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EventResponse>> getAllEvent() {
        return ResponseEntity.ok(eventService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(@RequestBody EventUpdateRequest payload, @PathVariable UUID id, Authentication authentication) {
        if(authentication.getPrincipal() == null ) throw new NullPointerException("login first to access this resource");
        UserContext userContext = (UserContext) authentication.getPrincipal();
        return ResponseEntity.ok(eventService.update(userContext.getAccountId(),id,payload));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> deleteEvent(@PathVariable UUID id, Authentication authentication) {
        if(authentication.getPrincipal() == null ) throw new NullPointerException("login first to access this resource");
        UserContext userContext = (UserContext) authentication.getPrincipal();
        eventService.delete(userContext.getAccountId(),id);
        return ResponseEntity.ok("Event deleted successfully");
    }

}
