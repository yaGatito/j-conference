package com.epam.jconference.controller;

import com.epam.jconference.service.model.Event;
import com.epam.jconference.controller.dto.EventDto;
import com.epam.jconference.controller.dto.EventFilterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class EventController {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events")
    public EventDto create(@RequestBody EventDto eventDto){
        return null;
    }

    @GetMapping("/events")
    public List<EventDto> findAll(@RequestBody EventFilterDto eventFilter){
        return null;
    }

    @GetMapping("/events/{id}")
    public EventDto findById(@PathVariable Long id){
        return null;
    }

    @PatchMapping("/events")
    public EventDto update(@RequestBody EventDto eventDto){
        return null;
    }

    @DeleteMapping("/events")
    public EventDto delete(@RequestBody EventDto eventDto){
        return null;
    }

    @PatchMapping("/events/{id}")
    public EventDto deleteById(@PathVariable Long id){
        return null;
    }

    @GetMapping("/events/participation")
    public List<Event> participation(){
        return null;
    }
}
