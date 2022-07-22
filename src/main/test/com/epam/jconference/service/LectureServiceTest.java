package com.epam.jconference.service;

import com.epam.jconference.bean.Session;
import com.epam.jconference.dto.LectureDto;
import com.epam.jconference.exception.EntityNotFoundException;
import com.epam.jconference.exception.InvalidOperationException;
import com.epam.jconference.mapper.EventMapper;
import com.epam.jconference.mapper.LectureMapper;
import com.epam.jconference.mapper.UserMapper;
import com.epam.jconference.model.Event;
import com.epam.jconference.model.Lecture;
import com.epam.jconference.model.User;
import com.epam.jconference.model.enums.LectureStatus;
import com.epam.jconference.model.enums.RequestStatus;
import com.epam.jconference.model.enums.UserRole;
import com.epam.jconference.repository.EventRepository;
import com.epam.jconference.repository.LectureRepository;
import com.epam.jconference.repository.RequestManagementRepository;
import com.epam.jconference.repository.UserRepository;
import com.epam.jconference.service.impl.LectureServiceImpl;
import com.epam.jconference.test.utils.TestLectureDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.epam.jconference.test.utils.TestEventDataUtil.ID;
import static com.epam.jconference.test.utils.TestEventDataUtil.createEvent;
import static com.epam.jconference.test.utils.TestLectureDataUtil.createLecture;
import static com.epam.jconference.test.utils.TestLectureDataUtil.createLectureDto;
import static com.epam.jconference.test.utils.TestUserDataUtil.createUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LectureServiceTest {

    @Mock
    private LectureRepository lectureRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private RequestManagementRepository requestManagementRepository;
    @Spy
    private Session session = new Session();
    @InjectMocks
    private LectureServiceImpl lectureService;

    @Test
    void createSecuredOrOfferLectureTest() {
        Event event = createEvent();
        User user = createUser();
        Lecture lecture = TestLectureDataUtil.createLecture();
        when(eventRepository.existsById(any())).thenReturn(true);
        when(eventRepository.getById(any())).thenReturn(event);
        when(userRepository.existsById(any())).thenReturn(true);
        when(userRepository.getById(any())).thenReturn(user);
        when(lectureRepository.existsByEventAndSpeakerAndTopic(any(), any(), anyString())).thenReturn(false);
        when(lectureRepository.save(any())).thenReturn(lecture);

        LectureDto lectureDto = createLectureDto();
        LectureDto responseLectureDto = lectureService.create(lectureDto);

        assertThat(responseLectureDto, allOf(hasProperty("id", equalTo(lecture.getId())),
                hasProperty("topic", equalTo(lectureDto.getTopic())), hasProperty("status",
                        equalTo(lectureDto.getStatus())), hasProperty("speaker",
                        equalTo(UserMapper.INSTANCE.mapUserDto(user))), hasProperty("event",
                        equalTo(EventMapper.INSTANCE.mapToDto(event)))));
        verify(eventRepository, times(1)).updateQuantityOfLectures(any());
    }

    @Test
    void createSecuredOrOfferLectureAlreadyExistedTest() {
        Event event = createEvent();
        User user = createUser();
        when(eventRepository.existsById(any())).thenReturn(true);
        when(eventRepository.getById(any())).thenReturn(event);
        when(userRepository.existsById(any())).thenReturn(true);
        when(userRepository.getById(any())).thenReturn(user);
        when(lectureRepository.existsByEventAndSpeakerAndTopic(any(), any(), anyString())).thenReturn(true);

        LectureDto lectureDto = createLectureDto();

        assertThrows(InvalidOperationException.class, () -> lectureService.create(lectureDto));
        verify(lectureRepository, times(0)).save(any());
    }

    @Test
    void createRejectedLectureTest() {
        LectureDto lectureDto = createLectureDto();
        lectureDto.setStatus(LectureStatus.REJECTED);

        assertThrows(InvalidOperationException.class, () -> lectureService.create(lectureDto));
        verify(lectureRepository, times(0)).save(any());
    }

    @Test
    void createRequestLectureTest() {
        LectureDto lectureDto = createLectureDto();
        lectureDto.setStatus(LectureStatus.REQUEST);

        assertThrows(InvalidOperationException.class, () -> lectureService.create(lectureDto));
        verify(lectureRepository, times(0)).save(any());
    }

    @Test
    void createFreeLectureTest() {
        Event event = createEvent();
        User user = createUser();
        Lecture lecture = TestLectureDataUtil.createLecture();
        lecture.setSpeaker(null);
        lecture.setStatus(LectureStatus.FREE);
        when(eventRepository.existsById(any())).thenReturn(true);
        when(eventRepository.getById(any())).thenReturn(event);
        when(lectureRepository.existsFreeLecture(any(), anyString())).thenReturn(0);
        when(lectureRepository.save(any())).thenReturn(lecture);

        LectureDto lectureDto = createLectureDto();
        lectureDto.setSpeaker(null);
        lectureDto.setStatus(LectureStatus.FREE);
        LectureDto responseLectureDto = lectureService.create(lectureDto);

        assertThat(responseLectureDto, allOf(hasProperty("id", equalTo(lecture.getId())),
                hasProperty("topic", equalTo(lectureDto.getTopic())), hasProperty("status",
                        equalTo(lectureDto.getStatus())), hasProperty("speaker", nullValue()),
                hasProperty("event", equalTo(EventMapper.INSTANCE.mapToDto(event)))));
        verify(eventRepository, times(1)).updateQuantityOfLectures(any());
    }

    @Test
    void createSecuredLectureWithNonexistentUserTest() {
        Event event = createEvent();
        when(eventRepository.existsById(any())).thenReturn(true);
        when(eventRepository.getById(any())).thenReturn(event);
        when(userRepository.existsById(any())).thenReturn(false);

        LectureDto lectureDto = createLectureDto();
        assertThrows(EntityNotFoundException.class, () -> lectureService.create(lectureDto));
        verify(lectureRepository, times(0)).save(any());
    }

    @Test
    void createSecuredLectureWithNonexistentEventTest() {
        when(eventRepository.existsById(any())).thenReturn(false);

        LectureDto lectureDto = createLectureDto();
        assertThrows(EntityNotFoundException.class, () -> lectureService.create(lectureDto));
        verify(lectureRepository, times(0)).save(any());
    }

    @Test
    void createFreeLectureWithSpeakerTest() {
        Event event = createEvent();
        when(eventRepository.existsById(any())).thenReturn(true);
        when(eventRepository.getById(any())).thenReturn(event);

        LectureDto lectureDto = createLectureDto();
        lectureDto.setStatus(LectureStatus.FREE);

        assertThrows(InvalidOperationException.class, () -> lectureService.create(lectureDto));
        verify(lectureRepository, times(0)).save(any());
    }

    @Test
    void createAlreadyExistedFreeLectureTest() {
        Event event = createEvent();
        Lecture lecture = TestLectureDataUtil.createLecture();
        lecture.setSpeaker(null);
        lecture.setStatus(LectureStatus.FREE);
        when(eventRepository.existsById(any())).thenReturn(true);
        when(eventRepository.getById(any())).thenReturn(event);
        when(lectureRepository.existsFreeLecture(any(), anyString())).thenReturn(1);

        LectureDto lectureDto = createLectureDto();
        lectureDto.setSpeaker(null);
        lectureDto.setStatus(LectureStatus.FREE);

        assertThrows(InvalidOperationException.class, () -> lectureService.create(lectureDto));
        verify(lectureRepository, times(0)).save(any());
    }

    @Test
    void getLectureByIdTest() {
        Lecture lecture = createLecture();
        when(lectureRepository.existsById(ID)).thenReturn(true);
        when(lectureRepository.getById(any())).thenReturn(lecture);
        lectureService.getById(ID);
        assertEquals(ID, lecture.getId());
    }

    @Test
    void getLectureByIdNotExistedTest() {
        when(lectureRepository.existsById(ID)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> lectureService.getById(ID));
    }

    @Test
    void assignSpeakerForFreeLectureTest() {
        User user = createUser();
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.FREE);
        lecture.setSpeaker(null);
        when(userRepository.existsById(any())).thenReturn(true);
        when(userRepository.getById(any())).thenReturn(user);
        when(lectureRepository.existsById(any())).thenReturn(true);
        when(lectureRepository.getById(any())).thenReturn(lecture);
        when(requestManagementRepository.appliedFreeLectures(any())).thenReturn(List.of(lecture.getId()));
        when(lectureRepository.save(any())).thenReturn(lecture);

        LectureDto lectureDto = lectureService.assignSpeakerForFreeLecture(ID, ID);

        verify(requestManagementRepository, times(1)).assignSpeakerOnFreeLecture(any(), any());
        assertThat(lectureDto, allOf(hasProperty("speaker",
                equalTo(UserMapper.INSTANCE.mapUserDto(createUser()))), hasProperty("status",
                equalTo(LectureStatus.SECURED))));
    }

    @Test
    void assignDoesntExistedSpeakerForFreeLectureTest() {
        when(userRepository.existsById(any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> lectureService.assignSpeakerForFreeLecture(ID, ID));
        verify(requestManagementRepository, times(0)).assignSpeakerOnFreeLecture(any(), any());
        verify(lectureRepository, times(0)).save(any());
    }

    @Test
    void assignSpeakerForDoesntExistedFreeLectureTest() {
        User user = createUser();
        when(userRepository.existsById(any())).thenReturn(true);
        when(userRepository.getById(any())).thenReturn(user);
        when(lectureRepository.existsById(any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> lectureService.assignSpeakerForFreeLecture(ID, ID));
        verify(requestManagementRepository, times(0)).assignSpeakerOnFreeLecture(any(), any());
        verify(lectureRepository, times(0)).save(any());
    }

    @Test
    void assignSpeakerForNotFreeLectureTest() {
        User user = createUser();
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.SECURED);
        when(userRepository.existsById(any())).thenReturn(true);
        when(userRepository.getById(any())).thenReturn(user);
        when(lectureRepository.existsById(any())).thenReturn(true);
        when(lectureRepository.getById(any())).thenReturn(lecture);

        assertThrows(InvalidOperationException.class,
                () -> lectureService.assignSpeakerForFreeLecture(ID, ID));
        verify(requestManagementRepository, times(0)).assignSpeakerOnFreeLecture(any(), any());
        verify(lectureRepository, times(0)).save(any());
    }

    @Test
    void assignSpeakerForFreeLectureWithDefinedSpeakerTest() {
        User user = createUser();
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.FREE);
        lecture.setSpeaker(user);
        when(userRepository.existsById(any())).thenReturn(true);
        when(userRepository.getById(any())).thenReturn(user);
        when(lectureRepository.existsById(any())).thenReturn(true);
        when(lectureRepository.getById(any())).thenReturn(lecture);

        assertThrows(InvalidOperationException.class,
                () -> lectureService.assignSpeakerForFreeLecture(ID, ID));
        verify(requestManagementRepository, times(0)).assignSpeakerOnFreeLecture(any(), any());
        verify(lectureRepository, times(0)).save(any());
    }

    @Test
    void assignNotAppliedSpeakerForFreeLectureTest() {
        User user = createUser();
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.FREE);
        lecture.setSpeaker(null);
        when(userRepository.existsById(any())).thenReturn(true);
        when(userRepository.getById(any())).thenReturn(user);
        when(lectureRepository.existsById(any())).thenReturn(true);
        when(lectureRepository.getById(any())).thenReturn(lecture);
        when(requestManagementRepository.appliedFreeLectures(any())).thenReturn(List.of());

        assertThrows(InvalidOperationException.class,
                () -> lectureService.assignSpeakerForFreeLecture(ID, ID));
        verify(requestManagementRepository, times(0)).assignSpeakerOnFreeLecture(any(), any());
        verify(lectureRepository, times(0)).save(any());
    }

    @Test
    void rejectRequestTest() {
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.REQUEST);
        when(lectureRepository.existsById(ID)).thenReturn(true);
        when(lectureRepository.getById(ID)).thenReturn(lecture);
        when(lectureRepository.save(any())).thenReturn(lecture);

        LectureDto rejectedRequest = lectureService.rejectRequest(ID);

        assertEquals(rejectedRequest.getStatus(), LectureStatus.REJECTED);
    }


    @Test
    void rejectDoesntExistedRequestTest() {
        when(lectureRepository.existsById(ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> lectureService.rejectRequest(ID));
    }


    @Test
    void rejectNotRequestedLectureTest() {
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.SECURED);
        when(lectureRepository.existsById(ID)).thenReturn(true);
        when(lectureRepository.getById(ID)).thenReturn(lecture);

        assertThrows(InvalidOperationException.class, () -> lectureService.rejectRequest(ID));
    }

    @Test
    void acceptRequestTest() {
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.REQUEST);
        when(lectureRepository.existsById(ID)).thenReturn(true);
        when(lectureRepository.getById(ID)).thenReturn(lecture);
        when(lectureRepository.save(any())).thenReturn(lecture);

        LectureDto acceptedRequest = lectureService.acceptRequest(ID);

        assertEquals(acceptedRequest.getStatus(), LectureStatus.SECURED);
    }


    @Test
    void acceptDoesntExistedRequestTest() {
        when(lectureRepository.existsById(ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> lectureService.rejectRequest(ID));
    }


    @Test
    void acceptNotRequestedLectureTest() {
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.SECURED);
        when(lectureRepository.existsById(ID)).thenReturn(true);
        when(lectureRepository.getById(ID)).thenReturn(lecture);

        assertThrows(InvalidOperationException.class, () -> lectureService.acceptRequest(ID));
    }

    @Test
    void moderHistoryTest() {
        User user = createUser();
        Event event = createEvent();
        Lecture rejectedLecture = createLecture();
        rejectedLecture.setStatus(LectureStatus.REJECTED);
        User speaker = User.builder()
                           .id(2L)
                           .lastname("LastNameDiff")
                           .name("NameDiff")
                           .email("email@gmail.com")
                           .notifications(true)
                           .role(UserRole.SPEAKER)
                           .build();
        Lecture rejectedFreeLecture = Lecture.builder()
                                             .event(event)
                                             .id(2L)
                                             .speaker(speaker)
                                             .status(LectureStatus.SECURED)
                                             .build();
        session.login(user);
        when(lectureRepository.findAllByStatus(LectureStatus.REJECTED)).thenReturn(List.of(rejectedLecture));
        when(lectureRepository.freeLecturesByRequestStatus(RequestStatus.REJECTED)).thenReturn(List.of(rejectedFreeLecture));

        List<LectureDto> moderHistory = lectureService.moderHistory();

        assertEquals(moderHistory.size(), 2);
        assertTrue(moderHistory.contains(LectureMapper.INSTANCE.mapToDto(rejectedLecture)));
        assertTrue(moderHistory.contains(LectureMapper.INSTANCE.mapToDto(rejectedFreeLecture)));
    }

    @Test
    void getSpeakerFreeLecturesTest() {
        User user = createUser();
        session.login(user);
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.FREE);
        lecture.setSpeaker(null);
        when(lectureRepository.findAllByStatusAndSpeaker(LectureStatus.FREE, user)).thenReturn(List.of(lecture));
        when(requestManagementRepository.appliedFreeLectures(user.getId())).thenReturn(List.of(user.getId()));

        List<LectureDto> freeLectures = lectureService.getLectures(LectureStatus.FREE, false);

        assertEquals(freeLectures.size(),
                freeLectures.stream().filter(l ->
                        l.getStatus().equals(LectureStatus.FREE)
                ).count()
        );
    }

    @Test
    void getModerFreeLecturesTest() {
        User user = createUser();
        session.login(user);
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.FREE);
        lecture.setSpeaker(null);
        when(lectureRepository.findAllByStatus(LectureStatus.FREE)).thenReturn(List.of(lecture));

        List<LectureDto> freeLectures = lectureService.getLectures(LectureStatus.FREE, true);

        assertEquals(freeLectures.size(),
                freeLectures.stream().filter(l -> l.getStatus().equals(LectureStatus.FREE))
                            .count());
    }

    @Test
    void getSpeakerSecuredLecturesTest() {
        LectureStatus status = LectureStatus.SECURED;
        User user = createUser();
        user.setRole(UserRole.SPEAKER);
        session.login(user);
        Lecture lecture = createLecture();
        lecture.setStatus(status);
        lecture.setSpeaker(user);
        when(lectureRepository.findAllByStatusAndSpeaker(status, user)).thenReturn(List.of(lecture));

        List<LectureDto> lectures = lectureService.getLectures(status, false);
        assertEquals(lectures.size(),
                lectures.stream().filter(l -> l.getStatus().equals(status)
                        && l.getSpeaker().equals(UserMapper.INSTANCE.mapUserDto(user))).count());
    }

    @Test
    void getModerSecuredLecturesTest() {
        LectureStatus status = LectureStatus.SECURED;
        User user = createUser();
        user.setRole(UserRole.MODER);
        session.login(user);
        Lecture lecture = createLecture();
        lecture.setStatus(status);
        when(lectureRepository.findAllByStatus(status)).thenReturn(List.of(lecture));

        List<LectureDto> lectures = lectureService.getLectures(status, true);
        assertEquals(lectures.size(),
                lectures.stream().filter(l -> l.getStatus().equals(status))
                        .count());
    }

    @Test
    void getSpeakerRequestsLecturesTest() {
        LectureStatus status = LectureStatus.REQUEST;
        User user = createUser();
        user.setRole(UserRole.SPEAKER);
        session.login(user);
        Lecture lecture = createLecture();
        lecture.setStatus(status);
        lecture.setSpeaker(user);
        when(lectureRepository.findAllByStatusAndSpeaker(status, user)).thenReturn(List.of(lecture));

        List<LectureDto> lectures = lectureService.getLectures(status, false);
        assertEquals(lectures.size(),
                lectures.stream().filter(l -> l.getStatus().equals(status)
                        && l.getSpeaker().equals(UserMapper.INSTANCE.mapUserDto(user))).count());
    }

    @Test
    void getModerRequestsLecturesTest() {
        LectureStatus status = LectureStatus.REQUEST;
        User user = createUser();
        user.setRole(UserRole.MODER);
        session.login(user);
        Lecture lecture = createLecture();
        lecture.setStatus(status);
        when(lectureRepository.findAllByStatus(status)).thenReturn(List.of(lecture));

        List<LectureDto> lectures = lectureService.getLectures(status, true);
        assertEquals(lectures.size(),
                lectures.stream().filter(l -> l.getStatus().equals(status))
                        .count());
    }

    @Test
    void getSpeakerOfferLecturesTest() {
        LectureStatus status = LectureStatus.OFFER;
        User user = createUser();
        user.setRole(UserRole.SPEAKER);
        session.login(user);
        Lecture lecture = createLecture();
        lecture.setStatus(status);
        lecture.setSpeaker(user);
        when(lectureRepository.findAllByStatusAndSpeaker(status, user)).thenReturn(List.of(lecture));

        List<LectureDto> lectures = lectureService.getLectures(status, false);
        assertEquals(lectures.size(),
                lectures.stream().filter(l -> l.getStatus().equals(status)
                        && l.getSpeaker().equals(UserMapper.INSTANCE.mapUserDto(user))).count());
    }

    @Test
    void getModerOfferLecturesTest() {
        LectureStatus status = LectureStatus.OFFER;
        User user = createUser();
        user.setRole(UserRole.MODER);
        session.login(user);
        Lecture lecture = createLecture();
        lecture.setStatus(status);
        when(lectureRepository.findAllByStatus(status)).thenReturn(List.of(lecture));

        List<LectureDto> lectures = lectureService.getLectures(status, true);
        assertEquals(lectures.size(),
                lectures.stream().filter(l -> l.getStatus().equals(status))
                        .count());
    }

    @Test
    void acceptOfferTest() {
        User user = createUser();
        session.login(user);
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.OFFER);
        lecture.setSpeaker(user);
        when(lectureRepository.existsById(any())).thenReturn(true);
        when(lectureRepository.getById(any())).thenReturn(lecture);
        when(lectureRepository.save(any())).thenReturn(lecture);

        LectureDto lectureDto = lectureService.acceptOffer(ID);

        assertEquals(LectureStatus.SECURED, lectureDto.getStatus());
        assertEquals(UserMapper.INSTANCE.mapUserDto(user), lectureDto.getSpeaker());
    }

    @Test
    void acceptNotExistedOfferLectureTest() {
        User user = createUser();
        session.login(user);
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.OFFER);
        lecture.setSpeaker(user);
        when(lectureRepository.existsById(any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> lectureService.acceptOffer(ID));
    }

    @Test
    void acceptNotOfferTest() {
        User user = createUser();
        session.login(user);
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.SECURED);
        lecture.setSpeaker(user);
        when(lectureRepository.existsById(any())).thenReturn(true);
        when(lectureRepository.getById(any())).thenReturn(lecture);

        assertThrows(InvalidOperationException.class, () -> lectureService.acceptOffer(ID));
    }

    @Test
    void acceptOfferNotForSessionUserTest() {
        User user = createUser();
        session.login(user);
        User speaker = User.builder()
                           .id(2L)
                           .lastname("LastNameDiff")
                           .name("NameDiff")
                           .email("email@gmail.com")
                           .password("qwerrty")
                           .notifications(true)
                           .role(UserRole.SPEAKER)
                           .build();
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.OFFER);
        lecture.setSpeaker(speaker);
        when(lectureRepository.existsById(any())).thenReturn(true);
        when(lectureRepository.getById(any())).thenReturn(lecture);

        assertThrows(InvalidOperationException.class, () -> lectureService.acceptOffer(ID));
    }

    @Test
    void rejectOfferTest() {
        User user = createUser();
        session.login(user);
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.OFFER);
        lecture.setSpeaker(user);
        when(lectureRepository.existsById(any())).thenReturn(true);
        when(lectureRepository.getById(any())).thenReturn(lecture);
        when(lectureRepository.save(any())).thenReturn(lecture);

        LectureDto lectureDto = lectureService.rejectOffer(ID);

        assertEquals(LectureStatus.REJECTED, lectureDto.getStatus());
        assertEquals(UserMapper.INSTANCE.mapUserDto(user), lectureDto.getSpeaker());
    }

    @Test
    void rejectNotExistedOfferLectureTest() {
        User user = createUser();
        session.login(user);
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.OFFER);
        lecture.setSpeaker(user);
        when(lectureRepository.existsById(any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> lectureService.rejectOffer(ID));
    }

    @Test
    void rejectNotOfferTest() {
        User user = createUser();
        session.login(user);
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.SECURED);
        lecture.setSpeaker(user);
        when(lectureRepository.existsById(any())).thenReturn(true);
        when(lectureRepository.getById(any())).thenReturn(lecture);

        assertThrows(InvalidOperationException.class, () -> lectureService.rejectOffer(ID));
    }

    @Test
    void rejectOfferNotForSessionUserTest() {
        User user = createUser();
        session.login(user);
        User speaker = User.builder()
                           .id(2L)
                           .lastname("LastNameDiff")
                           .name("NameDiff")
                           .email("email@gmail.com")
                           .password("qwerrty")
                           .notifications(true)
                           .role(UserRole.SPEAKER)
                           .build();
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.OFFER);
        lecture.setSpeaker(speaker);
        when(lectureRepository.existsById(any())).thenReturn(true);
        when(lectureRepository.getById(any())).thenReturn(lecture);

        assertThrows(InvalidOperationException.class, () -> lectureService.acceptOffer(ID));
    }

    @Test
    void addRequestLectureTest() {
        User user = createUser();
        session.login(user);
        Event event = createEvent();
        Lecture lecture = createLecture();
        when(eventRepository.existsById(any())).thenReturn(true);
        when(eventRepository.getById(any())).thenReturn(event);
        when(lectureRepository.existsByEventAndSpeakerAndTopic(any(), any(), anyString())).thenReturn(false);
        when(lectureRepository.save(any())).thenReturn(lecture);

        LectureDto lectureDto = createLectureDto();
        lectureDto.setStatus(null);
        lectureDto.setSpeaker(null);
        LectureDto persistedLecture = lectureService.addRequest(lectureDto);

        assertEquals(LectureStatus.SECURED, persistedLecture.getStatus());
        assertEquals(lectureDto.getEvent().getId(), persistedLecture.getEvent().getId());
        assertEquals(UserMapper.INSTANCE.mapUserDto(user), persistedLecture.getSpeaker());
    }

    @Test
    void addAlreadyExistedRequestLectureTest() {
        User user = createUser();
        session.login(user);
        Event event = createEvent();
        when(eventRepository.existsById(any())).thenReturn(true);
        when(eventRepository.getById(any())).thenReturn(event);
        when(lectureRepository.existsByEventAndSpeakerAndTopic(any(), any(), anyString())).thenReturn(true);

        LectureDto lectureDto = createLectureDto();
        lectureDto.setStatus(null);
        lectureDto.setSpeaker(null);

        assertThrows(InvalidOperationException.class, () -> lectureService.addRequest(lectureDto));
    }

    @Test
    void addRequestLectureToNotExistedEventTest() {
        User user = createUser();
        session.login(user);
        when(eventRepository.existsById(any())).thenReturn(false);

        LectureDto lectureDto = createLectureDto();
        lectureDto.setStatus(null);
        lectureDto.setSpeaker(null);

        assertThrows(EntityNotFoundException.class, () -> lectureService.addRequest(lectureDto));
    }

    @Test
    void applyFreeLectureTest() {
        User user = createUser();
        session.login(user);
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.FREE);
        lecture.setSpeaker(null);
        when(lectureRepository.existsById(any())).thenReturn(true);
        when(lectureRepository.getById(any())).thenReturn(lecture);
        when(requestManagementRepository.existsApplicationOnFreeLecture(user.getId(),
                lecture.getId())).thenReturn(false);

        lectureService.applyFreeLecture(lecture.getId());

        verify(requestManagementRepository, times(1)).applyOnFreeLecture(user.getId(),
                lecture.getId());
    }

    @Test
    void applyDoesntExistFreeLectureTest() {
        User user = createUser();
        session.login(user);
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.FREE);
        lecture.setSpeaker(null);
        when(lectureRepository.existsById(any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> lectureService.applyFreeLecture(lecture.getId()));
    }

    @Test
    void applyNotFreeLectureTest() {
        User user = createUser();
        session.login(user);
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.SECURED);
        when(lectureRepository.existsById(any())).thenReturn(true);
        when(lectureRepository.getById(any())).thenReturn(lecture);

        assertThrows(InvalidOperationException.class,
                () -> lectureService.applyFreeLecture(lecture.getId()));
    }

    @Test
    void applicationAlreadyExistOnFreeLectureTest() {
        User user = createUser();
        session.login(user);
        Lecture lecture = createLecture();
        lecture.setStatus(LectureStatus.FREE);
        lecture.setSpeaker(null);
        when(lectureRepository.existsById(any())).thenReturn(true);
        when(lectureRepository.getById(any())).thenReturn(lecture);
        when(requestManagementRepository.existsApplicationOnFreeLecture(user.getId(),
                lecture.getId())).thenReturn(true);

        assertThrows(InvalidOperationException.class,
                () -> lectureService.applyFreeLecture(lecture.getId()));
    }

    @Test
    void speakerHistoryTest() {
        User user = createUser();
        Event event = createEvent();
        Lecture rejectedLecture = createLecture();
        rejectedLecture.setStatus(LectureStatus.REJECTED);
        User speaker = User.builder()
                           .id(2L)
                           .lastname("LastNameDiff")
                           .name("NameDiff")
                           .email("email@gmail.com")
                           .notifications(true)
                           .role(UserRole.SPEAKER)
                           .build();
        Lecture rejectedFreeLecture = Lecture.builder()
                                             .event(event)
                                             .id(2L)
                                             .speaker(speaker)
                                             .status(LectureStatus.SECURED)
                                             .build();
        session.login(user);
        when(lectureRepository.findAllByStatusAndSpeaker(LectureStatus.REJECTED, user)).thenReturn(List.of(rejectedLecture));
        when(lectureRepository.freeLecturesByRequestStatusAndSpeaker(RequestStatus.REJECTED,
                user)).thenReturn(List.of(rejectedFreeLecture));

        List<LectureDto> speakerHistory = lectureService.speakerHistory();

        assertEquals(speakerHistory.size(), 2);
        assertTrue(speakerHistory.contains(LectureMapper.INSTANCE.mapToDto(rejectedLecture)));
        assertTrue(speakerHistory.contains(LectureMapper.INSTANCE.mapToDto(rejectedFreeLecture)));
        assertEquals(speakerHistory.size(), speakerHistory.stream().filter(l -> {
            return (l.getStatus().equals(LectureStatus.REJECTED) && l.getSpeaker()
                                                                     .getId()
                                                                     .equals(user.getId())) ||
                    (l.getStatus().equals(LectureStatus.SECURED) && !l.getSpeaker()
                                                                      .getId()
                                                                      .equals(user.getId()));
        }).count());
    }
}
