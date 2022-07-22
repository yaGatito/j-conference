package com.epam.jconference.controller;

import com.epam.jconference.config.TestConfig;
import com.epam.jconference.dto.LectureDto;
import com.epam.jconference.dto.UserDto;
import com.epam.jconference.exception.EntityNotFoundException;
import com.epam.jconference.exception.InvalidOperationException;
import com.epam.jconference.model.enums.ErrorType;
import com.epam.jconference.model.enums.LectureStatus;
import com.epam.jconference.service.LectureService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.epam.jconference.test.utils.TestLectureDataUtil.ID;
import static com.epam.jconference.test.utils.TestLectureDataUtil.createLectureDto;
import static com.epam.jconference.test.utils.TestUserDataUtil.createUserDto;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LectureController.class)
@Import(TestConfig.class)
public class LectureControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrlModer = "/api/v1/lectures/moder";
    private final String baseUrlSpeaker = "/api/v1/lectures/speaker";
    @MockBean
    private LectureService lectureService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void createLectureTest() throws Exception {
        LectureDto persisted = createLectureDto();
        persisted.setId(ID);
        LectureDto lectureDto = createLectureDto();
        when(lectureService.create(lectureDto)).thenReturn(persisted);

        String body = objectMapper.writeValueAsString(lectureDto);

        mockMvc.perform(post(baseUrlModer).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id").isNumber())
               .andExpect(jsonPath("$.topic").value(lectureDto.getTopic()))
               .andExpect(jsonPath("$.status").value(lectureDto.getStatus().name()))
               .andExpect(jsonPath("$.speaker.id").value(lectureDto.getSpeaker().getId()))
               .andExpect(jsonPath("$.event.id").value(lectureDto.getEvent().getId()));
    }

    @Test
    void createInvalidLectureTest() throws Exception {
        String errorType = ErrorType.VALIDATION_ERROR_TYPE.name();

        LectureDto lectureDto = createLectureDto();
        lectureDto.setId(1L);

        String body = objectMapper.writeValueAsString(lectureDto);

        mockMvc.perform(post(baseUrlModer).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(errorType)
               );

        lectureDto = createLectureDto();
        lectureDto.setEvent(null);

        body = objectMapper.writeValueAsString(lectureDto);

        mockMvc.perform(post(baseUrlModer).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(errorType)
               );

        lectureDto = createLectureDto();
        lectureDto.setTopic(null);

        body = objectMapper.writeValueAsString(lectureDto);

        mockMvc.perform(post(baseUrlModer).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(errorType)
               );

        lectureDto = createLectureDto();
        lectureDto.setStatus(LectureStatus.SECURED);
        lectureDto.setSpeaker(null);

        body = objectMapper.writeValueAsString(lectureDto);

        mockMvc.perform(post(baseUrlModer).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(errorType)
               );

        lectureDto = createLectureDto();
        lectureDto.setStatus(null);

        body = objectMapper.writeValueAsString(lectureDto);

        mockMvc.perform(post(baseUrlModer).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(errorType)
               );

    }

    @Test
    void createLectureThrowInvalidOperationExceptionTest() throws Exception {
        LectureDto lectureDto = createLectureDto();
        when(lectureService.create(lectureDto)).thenThrow(InvalidOperationException.class);

        String body = objectMapper.writeValueAsString(lectureDto);

        mockMvc.perform(post(baseUrlModer).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpect(status().is4xxClientError())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.errorType").value(ErrorType.INVALID_OPERATION_ERROR_TYPE.name()))
               .andExpect(jsonPath("$.timeStamp").isNotEmpty());
    }

    @Test
    void createLectureThrowEntityNotFoundExceptionTest() throws Exception {
        LectureDto lectureDto = createLectureDto();
        when(lectureService.create(lectureDto)).thenThrow(EntityNotFoundException.class);

        String body = objectMapper.writeValueAsString(lectureDto);

        mockMvc.perform(post(baseUrlModer).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpect(status().is4xxClientError())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.errorType").value(ErrorType.NOT_FOUND_ERROR_TYPE.name()))
               .andExpect(jsonPath("$.timeStamp").isNotEmpty());
    }

    @Test
    void findLectureByIdTest() throws Exception {
        LectureDto persisted = createLectureDto();
        persisted.setId(ID);
        when(lectureService.getById(any())).thenReturn(persisted);

        mockMvc.perform(get(baseUrlModer + "/" + ID))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id").value(ID))
               .andExpect(jsonPath("$.topic").value(persisted.getTopic()))
               .andExpect(jsonPath("$.status").value(persisted.getStatus().name()))
               .andExpect(jsonPath("$.speaker.id").value(persisted.getSpeaker().getId()))
               .andExpect(jsonPath("$.event.id").value(persisted.getEvent().getId()));
    }

    @Test
    void findNotExistedLectureByIdTest() throws Exception {
        LectureDto persisted = createLectureDto();
        persisted.setId(ID);
        when(lectureService.getById(any())).thenThrow(new EntityNotFoundException(any()));

        mockMvc.perform(get(baseUrlModer + "/" + ID))
               .andDo(print())
               .andExpect(status().is4xxClientError())
               .andExpect(jsonPath("$.errorType").value(ErrorType.NOT_FOUND_ERROR_TYPE.name()))
               .andExpect(jsonPath("$.timeStamp").isNotEmpty());
    }

    @Test
    void getSecuredLecturesForModerTest() throws Exception {
        LectureStatus status = LectureStatus.SECURED;
        LectureDto persisted = createLectureDto();
        persisted.setId(ID);
        persisted.setStatus(status);
        when(lectureService.getLectures(status, true)).thenReturn(List.of(persisted));

        mockMvc.perform(get(baseUrlModer + "/status/" + status.name()))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].status").value(status.name()));
    }

    @Test
    void getOfferLecturesForModerTest() throws Exception {
        LectureStatus status = LectureStatus.OFFER;
        LectureDto persisted = createLectureDto();
        persisted.setId(ID);
        persisted.setStatus(status);
        when(lectureService.getLectures(status, true)).thenReturn(List.of(persisted));

        mockMvc.perform(get(baseUrlModer + "/status/" + status.name()))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].status").value(status.name()));
    }

    @Test
    void getRequestLecturesForModerTest() throws Exception {
        LectureStatus status = LectureStatus.REQUEST;
        LectureDto persisted = createLectureDto();
        persisted.setId(ID);
        persisted.setStatus(status);
        when(lectureService.getLectures(status, true)).thenReturn(List.of(persisted));

        mockMvc.perform(get(baseUrlModer + "/status/" + status.name()))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].status").value(status.name()));
    }

    @Test
    void getFreeLecturesForModerTest() throws Exception {
        LectureStatus status = LectureStatus.FREE;
        LectureDto persisted = createLectureDto();
        persisted.setId(ID);
        persisted.setStatus(status);
        when(lectureService.getLectures(status, true)).thenReturn(List.of(persisted));

        mockMvc.perform(get(baseUrlModer + "/status/" + status.name()))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].status").value(status.name()));
    }

    @Test
    void getRejectedLecturesForModerTest() throws Exception {
        LectureStatus status = LectureStatus.REJECTED;
        LectureDto persisted = createLectureDto();
        persisted.setId(ID);
        persisted.setStatus(status);
        when(lectureService.getLectures(status, true)).thenReturn(List.of(persisted));

        mockMvc.perform(get(baseUrlModer + "/status/" + status.name()))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].status").value(status.name()));
    }

    @Test
    void assignSpeakerForFreeLectureTest() throws Exception {
        Long speakerId = ID;
        UserDto userDto = createUserDto();
        userDto.setId(speakerId);

        Long lectureId = 2L;
        LectureStatus status = LectureStatus.SECURED;
        LectureDto persisted = createLectureDto();
        persisted.setId(lectureId);
        persisted.setStatus(status);
        persisted.setSpeaker(userDto);

        when(lectureService.assignSpeakerForFreeLecture(speakerId, lectureId)).thenReturn(persisted);

        mockMvc.perform(post(baseUrlModer + "/free/assign?speaker=" + speakerId + "&free_lecture" + "=" + lectureId))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.status").value(status.name()))
               .andExpect(jsonPath("$.speaker.id").value(speakerId));
    }

    @Test
    void assignSpeakerForFreeLectureInvalidOperationExceptionTest() throws Exception {
        Long speakerId = ID;
        UserDto userDto = createUserDto();
        userDto.setId(speakerId);

        Long lectureId = 2L;
        LectureStatus status = LectureStatus.SECURED;
        LectureDto persisted = createLectureDto();
        persisted.setId(lectureId);
        persisted.setStatus(status);
        persisted.setSpeaker(userDto);

        when(lectureService.assignSpeakerForFreeLecture(speakerId, lectureId)).thenThrow(InvalidOperationException.class);

        mockMvc.perform(post(baseUrlModer + "/free/assign?speaker=" + speakerId + "&free_lecture" + "=" + lectureId))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().is4xxClientError())
               .andExpect(jsonPath("$.errorType").value(ErrorType.INVALID_OPERATION_ERROR_TYPE.name()));
    }

    @Test
    void rejectRequestTest() throws Exception {
        UserDto userDto = createUserDto();
        userDto.setId(ID);
        Long lectureId = 2L;
        LectureStatus status = LectureStatus.REJECTED;
        LectureDto persisted = createLectureDto();
        persisted.setId(lectureId);
        persisted.setStatus(status);
        persisted.setSpeaker(userDto);

        when(lectureService.rejectRequest(lectureId)).thenReturn(persisted);

        mockMvc.perform(post(baseUrlModer + "/requests/reject/" + lectureId))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.status").value(status.name()))
               .andExpect(jsonPath("$.id").value(lectureId));
    }

    @Test
    void acceptRequestTest() throws Exception {
        UserDto userDto = createUserDto();
        userDto.setId(ID);

        Long lectureId = 2L;
        LectureStatus status = LectureStatus.SECURED;
        LectureDto persisted = createLectureDto();
        persisted.setId(lectureId);
        persisted.setStatus(status);
        persisted.setSpeaker(userDto);

        when(lectureService.acceptRequest(lectureId)).thenReturn(persisted);


        mockMvc.perform(post(baseUrlModer + "/requests/accept/" + lectureId))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.status").value(status.name()))
               .andExpect(jsonPath("$.id").value(lectureId));
    }

    @Test
    void rejectRequestEntityNotFoundExceptionTest() throws Exception {
        Long lectureId = 2L;
        when(lectureService.rejectRequest(lectureId)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(post(baseUrlModer + "/requests/reject/" + lectureId))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().is4xxClientError())
               .andExpect(jsonPath("$.errorType").value(ErrorType.NOT_FOUND_ERROR_TYPE.name()));
    }

    @Test
    void acceptRequestEntityNotFoundExceptionTest() throws Exception {
        Long lectureId = 2L;
        when(lectureService.acceptRequest(lectureId)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(post(baseUrlModer + "/requests/accept/" + lectureId))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().is4xxClientError())
               .andExpect(jsonPath("$.errorType").value(ErrorType.NOT_FOUND_ERROR_TYPE.name()));
    }

    @Test
    void rejectRequestInvalidOperationExceptionTest() throws Exception {
        Long lectureId = 2L;
        when(lectureService.rejectRequest(lectureId)).thenThrow(InvalidOperationException.class);

        mockMvc.perform(post(baseUrlModer + "/requests/reject/" + lectureId))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().is4xxClientError())
               .andExpect(jsonPath("$.errorType").value(ErrorType.INVALID_OPERATION_ERROR_TYPE.name()));
    }

    @Test
    void acceptRequestInvalidOperationExceptionTest() throws Exception {
        Long lectureId = 2L;
        when(lectureService.acceptRequest(lectureId)).thenThrow(InvalidOperationException.class);

        mockMvc.perform(post(baseUrlModer + "/requests/accept/" + lectureId))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().is4xxClientError())
               .andExpect(jsonPath("$.errorType").value(ErrorType.INVALID_OPERATION_ERROR_TYPE.name()));
    }

    @Test
    void moderHistoryTest() throws Exception {
        LectureDto persisted = createLectureDto();
        persisted.setId(ID);
        persisted.setStatus(LectureStatus.REJECTED);
        when(lectureService.moderHistory()).thenReturn(List.of(persisted));

        mockMvc.perform(get(baseUrlModer + "/history"))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].status").value(LectureStatus.REJECTED.name()));
    }

    @Test
    void getSecuredLecturesForSpeakerTest() throws Exception {
        LectureStatus status = LectureStatus.SECURED;
        LectureDto persisted = createLectureDto();
        persisted.setId(ID);
        persisted.setStatus(status);
        when(lectureService.getLectures(status, false)).thenReturn(List.of(persisted));

        mockMvc.perform(get(baseUrlSpeaker + "/status/" + status.name()))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].status").value(status.name()));
    }

    @Test
    void getOfferLecturesForSpeakerTest() throws Exception {
        LectureStatus status = LectureStatus.OFFER;
        LectureDto persisted = createLectureDto();
        persisted.setId(ID);
        persisted.setStatus(status);
        when(lectureService.getLectures(status, false)).thenReturn(List.of(persisted));

        mockMvc.perform(get(baseUrlSpeaker + "/status/" + status.name()))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].status").value(status.name()));
    }

    @Test
    void getRequestLecturesForSpeakerTest() throws Exception {
        LectureStatus status = LectureStatus.REQUEST;
        LectureDto persisted = createLectureDto();
        persisted.setId(ID);
        persisted.setStatus(status);
        when(lectureService.getLectures(status, false)).thenReturn(List.of(persisted));

        mockMvc.perform(get(baseUrlSpeaker + "/status/" + status.name()))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].status").value(status.name()));
    }

    @Test
    void getFreeLecturesForSpeakerTest() throws Exception {
        LectureStatus status = LectureStatus.FREE;
        LectureDto persisted = createLectureDto();
        persisted.setId(ID);
        persisted.setStatus(status);
        when(lectureService.getLectures(status, false)).thenReturn(List.of(persisted));

        mockMvc.perform(get(baseUrlSpeaker + "/status/" + status.name()))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].status").value(status.name()));
    }

    @Test
    void applyFreeLectureTest() throws Exception {
        LectureStatus status = LectureStatus.FREE;
        LectureDto persisted = createLectureDto();
        persisted.setId(ID);
        persisted.setStatus(status);
        when(lectureService.applyFreeLecture(ID)).thenReturn(persisted);

        mockMvc.perform(post(baseUrlSpeaker + "/free/" + ID))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.status").value(status.name()));
    }

    @Test
    void applyFreeLectureInvalidOperationExceptionTest() throws Exception {
        LectureStatus status = LectureStatus.FREE;
        LectureDto persisted = createLectureDto();
        persisted.setId(ID);
        persisted.setStatus(status);
        when(lectureService.applyFreeLecture(ID)).thenThrow(InvalidOperationException.class);

        mockMvc.perform(post(baseUrlSpeaker + "/free/" + ID))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().is4xxClientError())
               .andExpect(jsonPath("$.errorType").value(ErrorType.INVALID_OPERATION_ERROR_TYPE.name()));
    }

    @Test
    void speakerHistoryTest() throws Exception {
        LectureDto persisted = createLectureDto();
        persisted.setId(ID);
        persisted.setStatus(LectureStatus.REJECTED);
        when(lectureService.speakerHistory()).thenReturn(List.of(persisted));

        mockMvc.perform(get(baseUrlSpeaker + "/history"))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].status").value(LectureStatus.REJECTED.name()));
    }


    @Test
    void rejectOfferTest() throws Exception {
        UserDto userDto = createUserDto();
        userDto.setId(ID);
        Long lectureId = 2L;
        LectureStatus status = LectureStatus.REJECTED;
        LectureDto persisted = createLectureDto();
        persisted.setId(lectureId);
        persisted.setStatus(status);
        persisted.setSpeaker(userDto);

        when(lectureService.rejectOffer(lectureId)).thenReturn(persisted);

        mockMvc.perform(post(baseUrlSpeaker + "/offers/reject/" + lectureId))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.status").value(status.name()))
               .andExpect(jsonPath("$.id").value(lectureId));
    }

    @Test
    void acceptOfferTest() throws Exception {
        UserDto userDto = createUserDto();
        userDto.setId(ID);
        Long lectureId = 2L;
        LectureStatus status = LectureStatus.SECURED;
        LectureDto persisted = createLectureDto();
        persisted.setId(lectureId);
        persisted.setStatus(status);
        persisted.setSpeaker(userDto);

        when(lectureService.acceptOffer(lectureId)).thenReturn(persisted);

        mockMvc.perform(post(baseUrlSpeaker + "/offers/accept/" + lectureId))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.status").value(status.name()))
               .andExpect(jsonPath("$.id").value(lectureId));
    }

    @Test
    void rejectOfferEntityNotFoundExceptionTest() throws Exception {
        Long lectureId = 2L;
        when(lectureService.rejectOffer(lectureId)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(post(baseUrlSpeaker + "/offers/reject/" + lectureId))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().is4xxClientError())
               .andExpect(jsonPath("$.errorType").value(ErrorType.NOT_FOUND_ERROR_TYPE.name()));
    }

    @Test
    void acceptOfferEntityNotFoundExceptionTest() throws Exception {
        Long lectureId = 2L;
        when(lectureService.acceptOffer(lectureId)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(post(baseUrlSpeaker + "/offers/accept/" + lectureId))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().is4xxClientError())
               .andExpect(jsonPath("$.errorType").value(ErrorType.NOT_FOUND_ERROR_TYPE.name()));
    }

    @Test
    void rejectOfferInvalidOperationExceptionTest() throws Exception {
        Long lectureId = 2L;
        when(lectureService.rejectOffer(lectureId)).thenThrow(InvalidOperationException.class);

        mockMvc.perform(post(baseUrlSpeaker + "/offers/reject/" + lectureId))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().is4xxClientError())
               .andExpect(jsonPath("$.errorType").value(ErrorType.INVALID_OPERATION_ERROR_TYPE.name()));
    }

    @Test
    void acceptOfferInvalidOperationExceptionTest() throws Exception {
        Long lectureId = 2L;
        when(lectureService.acceptOffer(lectureId)).thenThrow(InvalidOperationException.class);

        mockMvc.perform(post(baseUrlSpeaker + "/offers/accept/" + lectureId))
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().is4xxClientError())
               .andExpect(jsonPath("$.errorType").value(ErrorType.INVALID_OPERATION_ERROR_TYPE.name()));
    }

    @Test
    void addRequestTest() throws Exception {
        LectureDto persisted = createLectureDto();
        persisted.setId(ID);
        persisted.setStatus(LectureStatus.REQUEST);
        LectureDto lectureDto = createLectureDto();
        lectureDto.setStatus(null);
        lectureDto.setSpeaker(null);
        when(lectureService.addRequest(lectureDto)).thenReturn(persisted);

        String body = objectMapper.writeValueAsString(lectureDto);

        mockMvc.perform(post(baseUrlSpeaker + "/requests").contentType(MediaType.APPLICATION_JSON)
                                                          .content(body))
               .andDo(print())
               .andExpectAll(
                       status().isCreated(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$.id").isNumber(),
                       jsonPath("$.topic").value(lectureDto.getTopic()),
                       jsonPath("$.status").value(LectureStatus.REQUEST.name()),
                       jsonPath("$.speaker.id").value(persisted.getSpeaker().getId()),
                       jsonPath("$.event.id").value(lectureDto.getEvent().getId())
               );
    }

    @Test
    void addInvalidRequestTest() throws Exception {
        String errorType = ErrorType.VALIDATION_ERROR_TYPE.name();
        LectureDto lectureDto = createLectureDto();
        lectureDto.setStatus(LectureStatus.SECURED);
        lectureDto.setSpeaker(null);

        String body = objectMapper.writeValueAsString(lectureDto);

        mockMvc.perform(post(baseUrlSpeaker + "/requests")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(body))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(errorType)
               );

        lectureDto = createLectureDto();
        lectureDto.setStatus(null);
        lectureDto.setSpeaker(UserDto.builder().id(2L).build());

        body = objectMapper.writeValueAsString(lectureDto);

        mockMvc.perform(post(baseUrlSpeaker + "/requests")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(body))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(errorType)
               );

        lectureDto = createLectureDto();
        lectureDto.setTopic(null);
        lectureDto.setStatus(null);
        lectureDto.setSpeaker(null);

        body = objectMapper.writeValueAsString(lectureDto);

        mockMvc.perform(post(baseUrlSpeaker + "/requests")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(body))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(errorType)
               );

        lectureDto = createLectureDto();
        lectureDto.setEvent(null);
        lectureDto.setStatus(null);
        lectureDto.setSpeaker(null);

        body = objectMapper.writeValueAsString(lectureDto);

        mockMvc.perform(post(baseUrlSpeaker + "/requests")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(body))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(errorType)
               );

        lectureDto = createLectureDto();
        lectureDto.setId(2L);
        lectureDto.setStatus(null);
        lectureDto.setSpeaker(null);

        body = objectMapper.writeValueAsString(lectureDto);

        mockMvc.perform(post(baseUrlSpeaker + "/requests")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(body))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(errorType)
               );
    }
}
