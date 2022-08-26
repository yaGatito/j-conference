package com.epam.jconference.service.impl;

import com.epam.jconference.bean.Session;
import com.epam.jconference.dto.EventDto;
import com.epam.jconference.dto.EventPagingSortingFilterDto;
import com.epam.jconference.exception.EntityNotFoundException;
import com.epam.jconference.exception.InvalidOperationException;
import com.epam.jconference.exception.UnauthorizedAccessException;
import com.epam.jconference.mapper.EventMapper;
import com.epam.jconference.mapper.TagMapper;
import com.epam.jconference.model.Event;
import com.epam.jconference.model.EventListener;
import com.epam.jconference.model.User;
import com.epam.jconference.repository.EventRepository;
import com.epam.jconference.repository.impl.EventListenerRepositoryImpl;
import com.epam.jconference.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final Session session;
    private final EventListenerRepository eventListenerRepository;

    private final EventMapper mapper = EventMapper.INSTANCE;

    @Override
    public EventDto create(EventDto eventDto) {
        if (eventDto.getStartTime().isAfter(eventDto.getEndTime())) {
            throw new InvalidOperationException("End time can't be before start time");
        }
        Event event = mapper.mapToEntity(eventDto);
        event.setTags(eventDto.getTags().stream().map(TagMapper.INSTANCE::mapToEntity).collect(Collectors.toList()));
        event.setListeners(0);
        event.setLectures(0);
        return mapper.mapToDto(eventRepository.save(event));
    }

    @Override
    public List<EventDto> getAll(EventPagingSortingFilterDto filterDto) {
        Sort sort = Sort.by(filterDto.getOption().name().toLowerCase(Locale.ROOT));
        if (filterDto.getDescendingOrder()) {
            sort = sort.descending();
        }
        Pageable pageable = PageRequest.of(filterDto.getPage(), filterDto.NUMBER_OF_ELEMENTS, sort);
        Boolean futureEvents = filterDto.getFutureEvents();

        List<Event> persistedEntities;
        if (Objects.isNull(futureEvents)) {
            persistedEntities = eventRepository.findAll(pageable).getContent();
        } else if (futureEvents) {
            persistedEntities = eventRepository.findAllByDateAfter(LocalDate.now(), pageable);
        } else {
            persistedEntities = eventRepository.findAllByDateBefore(LocalDate.now(), pageable);
        }

        return persistedEntities.stream().map(mapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public EventDto getById(Long id) {
        return mapper.mapToDto(eventRepository.getById(id));
    }

    @Override
    public EventDto update(EventDto eventDto) {
        Long id = eventDto.getId();
        if (eventRepository.existsById(id)) {
            Event persistedEvent = eventRepository.getById(id);
            mapper.updateFieldsForEvent(eventDto, persistedEvent);
            if (persistedEvent.getStartTime().isAfter(persistedEvent.getEndTime())) {
                throw new InvalidOperationException("End time can't be before start time");
            }
            return mapper.mapToDto(eventRepository.save(persistedEvent));
        } else {
            throw new EntityNotFoundException("Event with ID: " + id + " doesn't exist");
        }
    }

    @Override
    public ResponseEntity<Void> deleteById(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new InvalidOperationException("Event doesn't exist");
        }
        eventRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public List<EventDto> participation() {
        if (Objects.nonNull(session.getUser())) {
            return eventRepository.participation(session.getUser()).stream().map(mapper::mapToDto)
                                  .collect(Collectors.toList());
        } else {
            throw new UnauthorizedAccessException("User must be logged in");
        }
    }

    @Override
    public ResponseEntity<Void> join(Long eventId) {
        session.isLogged();
        User authUser = session.getUser();
        EventDto eventDto = getById(eventId);
        Event persistedEvent = mapper.mapToEntity(eventDto);
        EventListener eventListener = new EventListener(persistedEvent, authUser);
        if (!eventListenerRepository.exists(eventListener)) {
            eventListenerRepository.add(eventListener);
            eventRepository.updateQuantityOfListeners(persistedEvent);
            return ResponseEntity.ok().build();
        } else {
            throw new InvalidOperationException("User is joined specified event already");
        }
    }

    @Override
    public ResponseEntity<Void> leave(Long eventId) {
        session.isLogged();
        User authUser = session.getUser();
        EventDto eventDto = getById(eventId);
        Event persistedEvent = mapper.mapToEntity(eventDto);
        EventListener eventListener = new EventListener(persistedEvent, authUser);
        if (eventListenerRepository.exists(eventListener)) {
            eventListenerRepository.remove(eventListener);
            eventRepository.updateQuantityOfListeners(persistedEvent);
            return ResponseEntity.ok().build();
        } else {
            throw new InvalidOperationException("User is not joined specified event yet");
        }
    }
}
