package com.epam.jconference.controller;

import com.epam.jconference.config.TestConfig;
import com.epam.jconference.dto.EventDto;
import com.epam.jconference.dto.EventPagingSortingFilterDto;
import com.epam.jconference.exception.EntityNotFoundException;
import com.epam.jconference.exception.InvalidOperationException;
import com.epam.jconference.mapper.EventMapper;
import com.epam.jconference.model.enums.ErrorType;
import com.epam.jconference.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.epam.jconference.test.utils.TestEventDataUtil.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EventController.class)
@Import(TestConfig.class)
public class EventControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final String baseUrl = "/api/v1/events";
    @MockBean
    private EventService eventService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void createEventTest() throws Exception {
        EventDto eventDto = createEventDto();
        EventDto persistedEvent = EventMapper.INSTANCE.mapToDto(createEvent());
        when(eventService.create(eventDto)).thenReturn(persistedEvent);

        String body = objectMapper.writeValueAsString(eventDto);

        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id").isNumber())
               .andExpect(jsonPath("$.topic").value(eventDto.getTopic()))
               .andExpect(jsonPath("$.date").value(eventDto.getDate().toString()))
               .andExpect(jsonPath("$.startTime").value(eventDto.getStartTime().format(DateTimeFormatter.ISO_TIME)))
               .andExpect(jsonPath("$.endTime").value(eventDto.getEndTime().format(DateTimeFormatter.ISO_TIME)))
               .andExpect(jsonPath("$.location").value(eventDto.getLocation()));
    }

    @Test
    void createInvalidEventTest() throws Exception {
        String errorType = ErrorType.VALIDATION_ERROR_TYPE.name();
        EventDto eventDto = createEventDto();
        eventDto.setTopic("");
        String body = objectMapper.writeValueAsString(eventDto);

        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(errorType)
               );
        eventDto = createEventDto();
        eventDto.setTopic(null);
        body = objectMapper.writeValueAsString(eventDto);

        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(errorType)
               );


        eventDto = createEventDto();
        eventDto.setEndTime(null);
        body = objectMapper.writeValueAsString(eventDto);

        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(errorType)
               );

        eventDto = createEventDto();
        eventDto.setStartTime(null);
        body = objectMapper.writeValueAsString(eventDto);

        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(errorType)
               );

        eventDto = createEventDto();
        eventDto.setDate(null);
        body = objectMapper.writeValueAsString(eventDto);

        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(errorType)
               );

        eventDto = createEventDto();
        eventDto.setTags(null);
        body = objectMapper.writeValueAsString(eventDto);

        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(errorType)
               );

        eventDto = createEventDto();
        eventDto.setLocation("");
        body = objectMapper.writeValueAsString(eventDto);

        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(errorType)
               );

        eventDto = createEventDto();
        eventDto.setLocation(null);
        body = objectMapper.writeValueAsString(eventDto);

        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(errorType)
               );

        eventDto = createEventDto();
        eventDto.setId(2L);
        body = objectMapper.writeValueAsString(eventDto);

        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(errorType)
               );
    }

    @Test
    void createEventInvalidOperationExceptionTest() throws Exception {
        EventDto eventDto = createEventDto();
        when(eventService.create(eventDto)).thenThrow(InvalidOperationException.class);

        String body = objectMapper.writeValueAsString(eventDto);

        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpect(status().is4xxClientError())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.errorType").value(ErrorType.INVALID_OPERATION_ERROR_TYPE.name()));
    }

    @Test
    void getByIdTest() throws Exception {
        EventDto persistedEvent = EventMapper.INSTANCE.mapToDto(createEvent());
        when(eventService.getById(ID)).thenReturn(persistedEvent);

        mockMvc.perform(get(baseUrl + "/" + ID))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id").value(ID))
               .andExpect(jsonPath("$.topic").isNotEmpty())
               .andExpect(jsonPath("$.date").isNotEmpty())
               .andExpect(jsonPath("$.startTime").isNotEmpty())
               .andExpect(jsonPath("$.endTime").isNotEmpty())
               .andExpect(jsonPath("$.location").isNotEmpty());
    }

    @Test
    void getByIdEntityNotFoundExceptionTest() throws Exception {
        when(eventService.getById(ID)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get(baseUrl + "/" + ID))
               .andDo(print())
               .andExpect(status().is4xxClientError())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.errorType").value(ErrorType.NOT_FOUND_ERROR_TYPE.name()));
    }

    @Test
    void getAllTest() throws Exception {
        EventDto persistedEvent = EventMapper.INSTANCE.mapToDto(createEvent());
        when(eventService.getAll(any())).thenReturn(List.of(persistedEvent));

        EventPagingSortingFilterDto filterDto = EventPagingSortingFilterDto.builder().build();
        String body = objectMapper.writeValueAsString(filterDto);

        mockMvc.perform(get(baseUrl).content(body).contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].id").value(ID))
               .andExpect(jsonPath("$[0].topic").isNotEmpty())
               .andExpect(jsonPath("$[0].date").isNotEmpty())
               .andExpect(jsonPath("$[0].startTime").isNotEmpty())
               .andExpect(jsonPath("$[0].endTime").isNotEmpty())
               .andExpect(jsonPath("$[0].location").isNotEmpty());
    }

    @Test
    void updateEventTest() throws Exception {
        EventDto eventDto = EventDto.builder().id(ID).location("new Location").build();
        EventDto persistedEvent = EventMapper.INSTANCE.mapToDto(createEvent());
        persistedEvent.setLocation(eventDto.getLocation());
        when(eventService.update(any())).thenReturn(persistedEvent);

        String body = objectMapper.writeValueAsString(eventDto);

        mockMvc.perform(patch(baseUrl).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id").value(ID))
               .andExpect(jsonPath("$.location").value(eventDto.getLocation()));
    }


    @Test
    void updateInvalidEventTest() throws Exception {
        String errorType = ErrorType.VALIDATION_ERROR_TYPE.name();
        EventDto eventDto = EventDto
                .builder()
                .id(ID)
                .location("")
                .build();

        String body = objectMapper.writeValueAsString(eventDto);

        mockMvc.perform(patch(baseUrl).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(errorType)
               );
    }

    @Test
    void updateEventEntityNotFoundExceptionTest() throws Exception {
        EventDto eventDto = EventDto.builder().id(ID).location("new Location").build();
        EventDto persistedEvent = EventMapper.INSTANCE.mapToDto(createEvent());
        persistedEvent.setLocation(eventDto.getLocation());
        when(eventService.update(any())).thenReturn(persistedEvent);

        String body = objectMapper.writeValueAsString(eventDto);

        when(eventService.update(any())).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(patch(baseUrl).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpect(status().is4xxClientError())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.errorType").value(ErrorType.NOT_FOUND_ERROR_TYPE.name()));
    }

    @Test
    void deleteByIdTest() throws Exception {
        when(eventService.deleteById(any())).thenReturn(ResponseEntity.ok().build());
        mockMvc.perform(delete(baseUrl + "/" + ID))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    void deleteByIdEntityNotFoundExceptionTest() throws Exception {
        when(eventService.deleteById(any())).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(delete(baseUrl + "/" + ID))
               .andDo(print())
               .andExpect(status().is4xxClientError())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.errorType").value(ErrorType.NOT_FOUND_ERROR_TYPE.name()));
    }

    @Test
    void participation() throws Exception {
        EventDto persistedEvent = EventMapper.INSTANCE.mapToDto(createEvent());
        when(eventService.participation()).thenReturn(List.of(persistedEvent));

        mockMvc.perform(get(baseUrl + "/participation"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].id").value(ID))
               .andExpect(jsonPath("$[0].topic").isNotEmpty())
               .andExpect(jsonPath("$[0].date").isNotEmpty())
               .andExpect(jsonPath("$[0].startTime").isNotEmpty())
               .andExpect(jsonPath("$[0].endTime").isNotEmpty())
               .andExpect(jsonPath("$[0].location").isNotEmpty());
    }

    @Test
    void joinTest() throws Exception {
        when(eventService.join(any())).thenReturn(ResponseEntity.ok().build());
        mockMvc.perform(post(baseUrl + "/join/" + ID))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    void joinTestEntityNotFoundExceptionTest() throws Exception {
        when(eventService.join(any())).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(post(baseUrl + "/join/" + ID))
               .andDo(print())
               .andExpect(status().is4xxClientError())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.errorType").value(ErrorType.NOT_FOUND_ERROR_TYPE.name()));
    }

    @Test
    void leaveTest() throws Exception {
        when(eventService.leave(any())).thenReturn(ResponseEntity.ok().build());
        mockMvc.perform(post(baseUrl + "/leave/" + ID))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    void leaveTestEntityNotFoundExceptionTest() throws Exception {
        when(eventService.leave(any())).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(post(baseUrl + "/leave/" + ID))
               .andDo(print())
               .andExpect(status().is4xxClientError())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.errorType").value(ErrorType.NOT_FOUND_ERROR_TYPE.name()));
    }
}
