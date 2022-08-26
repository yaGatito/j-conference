package com.epam.jconference.service;

import com.epam.jconference.dto.EventDto;
import com.epam.jconference.dto.EventPagingSortingFilterDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EventService {
    EventDto create(EventDto eventDto);

    List<EventDto> getAll(EventPagingSortingFilterDto eventFilterDto);

    EventDto getById(Long id);

    EventDto update(EventDto eventDto);

    ResponseEntity<Void> deleteById(Long id);

    List<EventDto> participation();

    ResponseEntity<Void> join(Long eventId);

    ResponseEntity<Void> leave(Long eventId);
}
