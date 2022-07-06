package com.epam.jconference.service.impl;

import com.epam.jconference.dto.EventDto;
import com.epam.jconference.dto.EventFilterDto;
import com.epam.jconference.mapper.EventMapper;
import com.epam.jconference.model.Event;
import com.epam.jconference.repository.EventRepository;
import com.epam.jconference.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public EventDto create(EventDto eventDto) {
        Event event = EventMapper.INSTANCE.mapToEntity(eventDto);
        event.setLectures(new ArrayList<>());
        event.setListeners(new ArrayList<>());
        return EventMapper.INSTANCE.mapToDto(eventRepository.create(event));
    }

    @Override
    public List<EventDto> getAll(EventFilterDto eventFilterDto) {
        return eventRepository.getAll(eventFilterDto).stream().map(EventMapper.INSTANCE::mapToDto).collect(Collectors.toList());
    }

    @Override
    public EventDto getById(Long id) {
        return EventMapper.INSTANCE.mapToDto(eventRepository.getById(id));
    }

    @Override
    public EventDto update(EventDto eventDto) {
        return EventMapper.INSTANCE.mapToDto(eventRepository.update(EventMapper.INSTANCE.mapToEntity(eventDto)));
    }

    @Override
    public ResponseEntity<Void> deleteById(Long id) {
        return eventRepository.deleteById(id);
    }

    @Override
    public List<EventDto> participation() {
        return eventRepository.participation().stream().map(EventMapper.INSTANCE::mapToDto).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<Void> join(Long eventId) {
        return eventRepository.join(eventId);
    }

    @Override
    public ResponseEntity<Void> leave(Long eventId) {
        return eventRepository.leave(eventId);
    }
}
