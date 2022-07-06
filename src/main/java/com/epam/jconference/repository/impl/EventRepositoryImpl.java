package com.epam.jconference.repository.impl;

import com.epam.jconference.bean.Session;
import com.epam.jconference.dto.EventDto;
import com.epam.jconference.dto.EventFilterDto;
import com.epam.jconference.exception.UnauthorizedAccessException;
import com.epam.jconference.mapper.EventMapper;
import com.epam.jconference.model.Event;
import com.epam.jconference.model.User;
import com.epam.jconference.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepository {

    private final Session session;
    private final List<Event> events = new ArrayList<>();

    @Override
    public Event create(Event event) {
        events.add(event);
        Long id = (long) events.indexOf(event);
        event.setId(id);
        return event;
    }

    @Override
    public List<Event> getAll(EventFilterDto eventFilterDto) {
        return events;
    }

    @Override
    public Event getById(Long id) {
        return events.get(id.intValue());
    }

    @Override
    public Event update(Event event) {
        Long id = event.getId();
        Event founded = events.get(id.intValue());
        founded.setDate(event.getDate());
        founded.setEndTime(event.getEndTime());
        founded.setStartTime(event.getStartTime());
        founded.setLectures(event.getLectures());
        founded.setTags(event.getTags());
        founded.setTopic(event.getTopic());
        return event;
    }

    @Override
    public ResponseEntity<Void> deleteById(Long id) {
        events.remove(events.get(id.intValue()));
        return ResponseEntity.noContent().build();
    }

    @Override
    public List<Event> participation() {
        User currentUser = session.getUser();
        if (currentUser == null) {
            throw new UnauthorizedAccessException("Authorize your account");
        }
        return events.stream().filter(
                event -> event.getListeners().contains(currentUser.getId())
        ).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<Void> join(Long eventId) {
        User currentUser = session.getUser();
        if (currentUser != null && events.size() > eventId && eventId >= 0) {
            Event event = events.get(eventId.intValue());
            if (!event.getListeners().contains(currentUser.getId())) {
                event.getListeners().add(currentUser.getId());
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<Void> leave(Long eventId) {
        User currentUser = session.getUser();
        if (currentUser != null && events.size() > eventId && eventId >= 0) {
            Event event = events.get(eventId.intValue());
            if (!event.getListeners().contains(currentUser.getId())) {
                event.getListeners().remove(currentUser.getId());
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
