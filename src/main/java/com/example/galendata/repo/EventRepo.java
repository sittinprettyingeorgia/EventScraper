package com.example.galendata.repo;

import com.example.galendata.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface EventRepo extends JpaRepository<Event, UUID> {
    Event getEventByName(String name);
    boolean existsById(UUID id);
}
