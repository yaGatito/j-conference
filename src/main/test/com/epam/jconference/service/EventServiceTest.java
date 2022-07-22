package com.epam.jconference.service;

import com.epam.jconference.bean.Session;
import com.epam.jconference.dto.EventDto;
import com.epam.jconference.dto.EventPagingSortingFilterDto;
import com.epam.jconference.exception.EntityNotFoundException;
import com.epam.jconference.exception.InvalidOperationException;
import com.epam.jconference.exception.UnauthorizedAccessException;
import com.epam.jconference.mapper.EventMapper;
import com.epam.jconference.model.Event;
import com.epam.jconference.model.User;
import com.epam.jconference.model.enums.SortEventOption;
import com.epam.jconference.repository.EventRepository;
import com.epam.jconference.repository.impl.EventListenerRepositoryImpl;
import com.epam.jconference.service.impl.EventServiceImpl;
import com.epam.jconference.test.utils.TestUserDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.jconference.test.utils.TestEventDataUtil.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Spy
    private final Session session = new Session();
    @Mock
    private EventListenerRepositoryImpl eventListenerRepository;
    @Mock
    private EventRepository eventRepository;
    @InjectMocks
    private EventServiceImpl eventService;


    @Test
    void createEventTest() {
        Event event = createEvent();
        EventDto eventDto = createEventDto();
        event.setLectures(0);
        event.setListeners(0);
        when(eventRepository.save(any())).thenReturn(event);

        EventDto persistedEventDto = eventService.create(eventDto);

        assertThat(persistedEventDto, allOf(
                hasProperty("id", equalTo(ID)),
                hasProperty("topic", equalTo(eventDto.getTopic())),
                hasProperty("location", equalTo(eventDto.getLocation())),
                hasProperty("startTime", equalTo(eventDto.getStartTime())),
                hasProperty("endTime", equalTo(eventDto.getEndTime())),
                hasProperty("date", equalTo(eventDto.getDate())),
                hasProperty("listeners", equalTo(0)),
                hasProperty("lectures", equalTo(0))));
    }

    @Test
    void createEventInvalidTimings() {
        EventDto eventDto = createEventDto();
        eventDto.setStartTime(LocalTime.of(10, 10));
        eventDto.setEndTime(LocalTime.of(8, 10));

        assertThrows(InvalidOperationException.class, () -> eventService.create(eventDto));
    }

    @Test
    void getAllEventsFutureBooleanNullTest() {
        List<Event> list = List.of(createEvent());
        when(eventRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(list));

        Boolean future = null;

        EventPagingSortingFilterDto filterDto = EventPagingSortingFilterDto.builder()
                                                                           .futureEvents(future)
                                                                           .descendingOrder(true)
                                                                           .option(SortEventOption.DATE)
                                                                           .page(0).build();
        List<EventDto> all = eventService.getAll(filterDto);

        verify(eventRepository, times(0)).findAllByDateBefore(any(), any());
        verify(eventRepository, times(0)).findAllByDateAfter(any(), any());
        verify(eventRepository, times(1)).findAll(any(Pageable.class));
        assertEquals(1, all.size());
        assertNotNull(all.get(0));
    }

    @Test
    void getAllEventsFutureBooleanTrueTest() {
        List<Event> list = List.of(createEvent());
        when(eventRepository.findAllByDateAfter(any(), any())).thenReturn(list);

        Boolean future = true;

        EventPagingSortingFilterDto filterDto = EventPagingSortingFilterDto.builder()
                                                                           .futureEvents(future)
                                                                           .descendingOrder(true)
                                                                           .option(SortEventOption.DATE)
                                                                           .page(0).build();
        List<EventDto> all = eventService.getAll(filterDto);


        verify(eventRepository, times(0)).findAllByDateBefore(any(), any());
        verify(eventRepository, times(1)).findAllByDateAfter(any(), any());
        verify(eventRepository, times(0)).findAll(any(Pageable.class));
        assertEquals(1, all.size());
        assertNotNull(all.get(0));
    }

    @Test
    void getAllEventsFutureBooleanFalseTest() {
        List<Event> list = List.of(createEvent());
        when(eventRepository.findAllByDateBefore(any(), any())).thenReturn(list);

        Boolean future = false;

        EventPagingSortingFilterDto filterDto = EventPagingSortingFilterDto.builder()
                                                                           .futureEvents(future)
                                                                           .descendingOrder(true)
                                                                           .option(SortEventOption.DATE)
                                                                           .page(0).build();
        List<EventDto> all = eventService.getAll(filterDto);

        verify(eventRepository, times(1)).findAllByDateBefore(any(), any());
        verify(eventRepository, times(0)).findAllByDateAfter(any(), any());
        verify(eventRepository, times(0)).findAll(any(Pageable.class));
        assertEquals(1, all.size());
        assertNotNull(all.get(0));
    }

    @Test
    void getEventByIdTest() {
        Event event = createEvent();
        when(eventRepository.getById(ID)).thenReturn(event);

        EventDto fetchedEventDto = eventService.getById(ID);

        assertEquals(ID, fetchedEventDto.getId());
        assertThat(fetchedEventDto, allOf(
                hasProperty("id", notNullValue()),
                hasProperty("topic", notNullValue()),
                hasProperty("location", notNullValue()),
                hasProperty("date", notNullValue()),
                hasProperty("startTime", notNullValue()),
                hasProperty("endTime", notNullValue()),
                hasProperty("listeners", notNullValue()),
                hasProperty("lectures", notNullValue())
        ));
    }

    @Test
    void updateEventTest() {
        EventDto eventDto = createEventDto();
        eventDto.setId(1L);
        eventDto.setDate(LocalDate.of(2022, 12, 21));
        eventDto.setStartTime(null);
        eventDto.setEndTime(null);
        eventDto.setLocation(null);
        eventDto.setTags(null);

        Event event = createEvent();
        event.setDate(LocalDate.of(2022, 12, 21));

        when(eventRepository.existsById(ID)).thenReturn(true);
        when(eventRepository.getById(eventDto.getId())).thenReturn(event);
        when(eventRepository.save(any())).thenReturn(event);

        EventDto updatedEvent = eventService.update(eventDto);

        assertThat(updatedEvent, allOf(
                hasProperty("startTime", notNullValue()),
                hasProperty("endTime", notNullValue()),
                hasProperty("location", notNullValue()),
                hasProperty("date", equalTo(eventDto.getDate()))
        ));
    }

    @Test
    void updateEventInvalidTimings() {
        EventDto eventDto = createEventDto();
        eventDto.setStartTime(LocalTime.of(10, 10));
        eventDto.setEndTime(LocalTime.of(8, 10));
        eventDto.setId(1L);
        eventDto.setDate(LocalDate.of(2022, 12, 21));
        eventDto.setLocation(null);
        eventDto.setTags(null);

        Event event = createEvent();
        event.setDate(LocalDate.of(2022, 12, 21));

        when(eventRepository.existsById(ID)).thenReturn(true);
        when(eventRepository.getById(eventDto.getId())).thenReturn(event);

        assertThrows(InvalidOperationException.class, () -> eventService.update(eventDto));
    }

    @Test
    void updateEventEntityNotFoundExceptionTest() {
        when(eventRepository.existsById(any())).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> eventService.update(createEventDto()));
    }

    @Test
    void deleteByIdTest() {
        when(eventRepository.existsById(any())).thenReturn(true);

        eventService.deleteById(ID);

        verify(eventRepository, times(1)).deleteById(any());
    }

    @Test
    void deleteByIdEntityNotFoundTest() {
        when(eventRepository.existsById(any())).thenReturn(false);
        assertThrows(InvalidOperationException.class, () -> eventService.deleteById(any()));
    }

    @Test
    void participationTest() {
        User user = TestUserDataUtil.createUser();
        session.login(user);
        List<Event> events = List.of(createEvent());
        when(eventRepository.participation(any())).thenReturn(events);

        List<EventDto> participation = eventService.participation();
        assertEquals(participation.stream()
                                  .map(EventMapper.INSTANCE::mapToEntity)
                                  .collect(Collectors.toList()), events);
    }

    @Test
    void participationUnauthorizedAccessTest() {
        assertThrows(UnauthorizedAccessException.class, () -> eventService.participation());
    }

    @Test
    void joinTest() {
        User user = TestUserDataUtil.createUser();
        session.login(user);
        when(eventListenerRepository.exists(any())).thenReturn(false);

        eventService.join(ID);

        verify(eventListenerRepository, times(1)).add(any());
        verify(eventRepository, times(1)).updateQuantityOfListeners(any());
    }

    @Test
    void joinExistedEventListenerTest() {
        User user = TestUserDataUtil.createUser();
        session.login(user);
        when(eventListenerRepository.exists(any())).thenReturn(true);
        assertThrows(InvalidOperationException.class, () -> eventService.join(ID));
    }

    @Test
    void joinUnauthorizedUserTest() {
        assertThrows(UnauthorizedAccessException.class, () -> eventService.join(ID));
    }

    @Test
    void leaveTest() {
        User user = TestUserDataUtil.createUser();
        session.login(user);
        when(eventListenerRepository.exists(any())).thenReturn(true);

        eventService.leave(ID);

        verify(eventListenerRepository, times(1)).remove(any());
        verify(eventRepository, times(1)).updateQuantityOfListeners(any());
    }

    @Test
    void leaveNotExistedEventListenerTest() {
        User user = TestUserDataUtil.createUser();
        session.login(user);
        when(eventListenerRepository.exists(any())).thenReturn(false);
        assertThrows(InvalidOperationException.class, () -> eventService.leave(ID));
    }

    @Test
    void leaveUnauthorizedUserTest() {
        assertThrows(UnauthorizedAccessException.class, () -> eventService.leave(ID));
    }
}
