package com.epam.jconference.repository;

import com.epam.jconference.dto.EventDto;
import com.epam.jconference.dto.EventFilterDto;
import com.epam.jconference.model.Event;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EventRepository {

    Event create(Event event);

    List<Event> getAll(EventFilterDto eventFilterDto);

    Event getById(Long id);

    Event update(Event event);

    ResponseEntity<Void> deleteById(Long id);

    List<Event> participation();

    ResponseEntity<Void> join(Long eventId);

    ResponseEntity<Void> leave(Long eventId);
}
