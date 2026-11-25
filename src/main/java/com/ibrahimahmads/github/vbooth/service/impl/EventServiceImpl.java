package com.ibrahimahmads.github.vbooth.service.impl;

import com.ibrahimahmads.github.vbooth.dto.request.EventCreateRequest;
import com.ibrahimahmads.github.vbooth.dto.request.EventUpdateRequest;
import com.ibrahimahmads.github.vbooth.dto.request.EventUserSeach;
import com.ibrahimahmads.github.vbooth.dto.response.EventResponse;
import com.ibrahimahmads.github.vbooth.entity.Account;
import com.ibrahimahmads.github.vbooth.entity.Event;
import com.ibrahimahmads.github.vbooth.repository.EventRepository;
import com.ibrahimahmads.github.vbooth.service.AccountService;
import com.ibrahimahmads.github.vbooth.service.EventService;
import com.ibrahimahmads.github.vbooth.specification.EventAdminSpecification;
import com.ibrahimahmads.github.vbooth.specification.EventUserSpecification;
import com.ibrahimahmads.github.vbooth.utils.enums.EventStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final AccountService accountService;

    @Override
    public EventResponse save(UUID accountId, EventCreateRequest payload) {
        Account account = accountService.getAccountEntityById(accountId);

        if (payload.eventStartTime() != null && payload.eventEndTime() != null) {
            if (!payload.eventStartTime().isBefore(payload.eventEndTime())) {
                throw new IllegalArgumentException("Waktu mulai acara harus lebih awal dari waktu selesai");
            }
        }

        if (eventRepository.existsBySlug(payload.slug().toLowerCase().trim())) {
            throw new RuntimeException("Slug atau link event sudah digunakan, silakan pilih link lain");
        }

        Event event = Event.builder()
                .eventName(payload.eventName())
                .eventDescription(payload.eventDescription())
                .eventDate(payload.eventDate())
                .eventStartTime(payload.eventStartTime())
                .eventEndTime(payload.eventEndTime())
                .eventLocation(payload.eventLocation())
                .account(account)
                .slug(payload.slug().toLowerCase().trim())
                .eventStatus(EventStatus.DRAFT)
                .build();
        return eventRepository.save(event).toResponse();
    }

    @Override
    public EventResponse save(Event event) {
        return eventRepository.save(event).toResponse();
    }

    @Override
    public EventResponse getEventMeById(UUID id, UUID accountId) {
        Event event = eventRepository.findByIdAndAccount_Id(id,accountId).orElseThrow(()-> new RuntimeException("Event tidak ditemukan"));
        return event.toResponse();
    }

    @Override
    public EventResponse getEventById(UUID id) {
        Event event = eventRepository.findByid(id).orElseThrow(()-> new RuntimeException("Event tidak ditemukan"));
        return event.toResponse();
    }

    @Override
    public EventResponse update(UUID accountId, UUID id, EventUpdateRequest payload) {
        Event event = eventRepository.findByIdAndAccount_Id(id,accountId).orElseThrow(()-> new RuntimeException("Event tidak ditemukan"));

        if (event.getEventStatus() == EventStatus.COMPLETED || event.getEventStatus() == EventStatus.CANCELLED) {
            throw new IllegalStateException("Event yang sudah selesai atau dibatalkan tidak dapat diubah");
        }

        boolean isWithin48Hours = event.getEventDate().isBefore(LocalDate.now().plusDays(2));

        boolean isDateChanged = !event.getEventDate().isEqual(payload.eventDate());
        boolean isTimeChanged = (payload.eventStartTime() != null && !payload.eventStartTime().equals(event.getEventStartTime())) ||
                (payload.eventEndTime() != null && !payload.eventEndTime().equals(event.getEventEndTime()));

        if (isWithin48Hours && (isDateChanged || isTimeChanged)) {
            throw new IllegalStateException("Event tidak dapat diubah saat H-2 atau pada hari acara berlangsung");
        }

        if (payload.eventStartTime() != null && payload.eventEndTime() != null) {
            if (!payload.eventStartTime().isBefore(payload.eventEndTime())) {
                throw new IllegalArgumentException("Waktu mulai acara harus lebih awal dari waktu selesai");
            }
        }

        String newSlug = payload.slug().toLowerCase().trim();
        if (!event.getSlug().equalsIgnoreCase(newSlug)) {
            if (isWithin48Hours) {
                throw new IllegalStateException("Link event tidak dapat diubah saat mendekati hari H");
            }

            if (eventRepository.existsBySlug(newSlug)) {
                throw new RuntimeException("Slug atau link event sudah digunakan, silakan pilih link lain");
            }
            event.setSlug(newSlug);
        }

        event.setEventName(payload.eventName());
        event.setEventDescription(payload.eventDescription());
        event.setEventDate(payload.eventDate());
        event.setEventStartTime(payload.eventStartTime());
        event.setEventEndTime(payload.eventEndTime());
        event.setEventLocation(payload.eventLocation());
        Event updatedEvent = eventRepository.save(event);
        return updatedEvent.toResponse();
    }

    @Override
    public void delete(UUID id, UUID accountId) {
        Event event = eventRepository.findByIdAndAccount_Id(id,accountId).orElseThrow(()-> new RuntimeException("Event tidak ditemukan"));
        List<EventStatus> deletableStatuses = List.of(
                EventStatus.DRAFT,
                EventStatus.CANCELLED
        );

        if (!deletableStatuses.contains(event.getEventStatus())) {
            throw new IllegalStateException("Event yang sudah aktif atau selesai tidak dapat dihapus. Anda hanya dapat mengarsipkannya.");
        }
        eventRepository.delete(event);
    }

    @Override
    public Page<EventResponse> getAll(Pageable pageable, EventUserSeach search) {
        Specification<Event> specification = EventAdminSpecification.getSpecification(search);
        return eventRepository.findAll(specification,pageable).map(Event::toResponse);
    }

    @Override
    public Page<EventResponse> getAllByAccountId(UUID accountId, Pageable pageable, EventUserSeach search) {
        Specification<Event> specification = EventUserSpecification.getSpecification(accountId, search);
        return eventRepository.findAll(specification, pageable).map(Event::toResponse);
    }
}
