package com.ibrahimahmads.github.vbooth.repository;

import com.ibrahimahmads.github.vbooth.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    Optional<Event> findByid(UUID id);
    Optional<Event> findByIdAndAccount_Id(UUID id, UUID accountId);
    Boolean existsBySlug(String slug);
}
