package com.ibrahimahmads.github.vbooth.repository;

import com.ibrahimahmads.github.vbooth.entity.Greetings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GreetingRepository extends JpaRepository<Greetings, UUID> {
    Greetings findByid(UUID id);
}
