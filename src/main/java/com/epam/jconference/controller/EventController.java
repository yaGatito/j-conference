package com.epam.jconference.controller;

import com.epam.jconference.api.EventApi;
import com.epam.jconference.dto.group.OnCreate;
import com.epam.jconference.dto.group.OnUpdate;
import com.epam.jconference.dto.EventDto;
import com.epam.jconference.dto.EventFilterDto;
import com.epam.jconference.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventController implements EventApi {

    private final EventService eventService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EventDto create(@RequestBody @Validated(OnCreate.class) EventDto eventDto) {
        return eventService.create(eventDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<EventDto> getAll(@RequestBody @Valid EventFilterDto eventFilter) {
        return eventService.getAll(eventFilter);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    public EventDto getById(@PathVariable Long id) {
        return eventService.getById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping
    public EventDto update(@RequestBody @Validated(OnUpdate.class) EventDto eventDto) {
        return eventService.update(eventDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        return eventService.deleteById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/participation")
    public List<EventDto> participation() {
        return eventService.participation();
    }

    @PostMapping("/join/{eventId}")
    public ResponseEntity<Void> join(@PathVariable Long eventId) {
        return eventService.join(eventId);
    }

    @PostMapping("/leave/{eventId}")
    public ResponseEntity<Void> leave(@PathVariable Long eventId) {
        return eventService.leave(eventId);
    }
}
