package com.ibrahimahmads.github.vbooth.controller;

import com.ibrahimahmads.github.vbooth.dto.request.EventUpdateRequest;
import com.ibrahimahmads.github.vbooth.dto.request.EventUserSeach;
import com.ibrahimahmads.github.vbooth.dto.response.EventResponse;
import com.ibrahimahmads.github.vbooth.dto.response.PageResponseWrapper;
import com.ibrahimahmads.github.vbooth.security.model.UserContext;
import com.ibrahimahmads.github.vbooth.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/me/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EventResponse> getMyEventDetail(@PathVariable UUID id, @AuthenticationPrincipal UserContext userContext) {
        return ResponseEntity.ok(eventService.getEventMeById(id,userContext.getAccountId()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> getEventDetail(@PathVariable UUID id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageResponseWrapper<EventResponse>> getAllMyEvent(@AuthenticationPrincipal UserContext userContext,
                                                                            @ModelAttribute EventUserSeach search,
                                                                            @RequestParam(name = "page", defaultValue = "1") Integer page,
                                                                            @RequestParam(name = "size", defaultValue = "5") Integer size,
                                                                            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
                                                                            @RequestParam(name = "direction", defaultValue = "DESC") String direction
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page-1, Math.min(size,50), sort);
        return ResponseEntity.ok(new PageResponseWrapper<>(eventService.getAllByAccountId(userContext.getAccountId(),pageable,search)));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponseWrapper<EventResponse>> getAllEvent(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                                           @RequestParam(name = "size", defaultValue = "5") Integer size,
                                                           @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
                                                           @RequestParam(name = "direction", defaultValue = "DESC") String direction,
                                                                          @ModelAttribute EventUserSeach search
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page-1, Math.min(size,50), sort);
        return ResponseEntity.ok(new PageResponseWrapper<>(eventService.getAll(pageable,search)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EventResponse> updateEvent(@RequestBody EventUpdateRequest payload, @PathVariable UUID id, @AuthenticationPrincipal UserContext userContext) {
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
