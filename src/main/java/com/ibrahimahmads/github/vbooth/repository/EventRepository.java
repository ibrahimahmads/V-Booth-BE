package com.ibrahimahmads.github.vbooth.repository;

import com.ibrahimahmads.github.vbooth.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> , JpaSpecificationExecutor<Event> {
    @EntityGraph(attributePaths = {"paket", "account"})
    Optional<Event> findByid(UUID id);
    @EntityGraph(attributePaths = {"paket", "account"})
    Optional<Event> findByIdAndAccount_Id(UUID id, UUID accountId);
    Boolean existsBySlug(String slug);
    @Override
    @EntityGraph(attributePaths = {"paket", "account"})
    Page<Event> findAll(Specification<Event> spec, Pageable pageable);
}
