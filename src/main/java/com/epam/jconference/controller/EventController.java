package com.epam.jconference.controller;

import com.epam.jconference.api.EventApi;
import com.epam.jconference.dto.EventDto;
import com.epam.jconference.dto.EventPagingSortingFilterDto;
import com.epam.jconference.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventController implements EventApi {

    private final EventService eventService;

    public EventDto create(EventDto eventDto) {
        return eventService.create(eventDto);
    }

    public List<EventDto> getAll(EventPagingSortingFilterDto eventFilter) {
        return eventService.getAll(eventFilter);
    }

    public EventDto getById(Long id) {
        return eventService.getById(id);
    }

    public EventDto update(EventDto eventDto) {
        return eventService.update(eventDto);
    }

    public ResponseEntity<Void> deleteById(Long id) {
        return eventService.deleteById(id);
    }

    public List<EventDto> participation() {
        return eventService.participation();
    }

    public ResponseEntity<Void> join(Long eventId) {
        return eventService.join(eventId);
    }

    public ResponseEntity<Void> leave(Long eventId) {
        return eventService.leave(eventId);
    }
}
