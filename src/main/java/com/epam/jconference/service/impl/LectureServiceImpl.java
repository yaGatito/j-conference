package com.epam.jconference.service.impl;

import com.epam.jconference.bean.Session;
import com.epam.jconference.dto.LectureDto;
import com.epam.jconference.exception.EntityNotFoundException;
import com.epam.jconference.exception.InvalidOperationException;
import com.epam.jconference.mapper.LectureMapper;
import com.epam.jconference.model.Event;
import com.epam.jconference.model.Lecture;
import com.epam.jconference.model.User;
import com.epam.jconference.model.enums.LectureStatus;
import com.epam.jconference.model.enums.RequestStatus;
import com.epam.jconference.repository.EventRepository;
import com.epam.jconference.repository.LectureRepository;
import com.epam.jconference.repository.RequestManagementRepository;
import com.epam.jconference.repository.UserRepository;
import com.epam.jconference.service.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestManagementRepository requestManagementRepository;
    private final Session session;

    private final LectureMapper mapper = LectureMapper.INSTANCE;

    @Override
    @Transactional
    public LectureDto create(LectureDto lectureDto) {
        Event persistedEvent = checkThenGet(lectureDto.getEvent().getId());
        Lecture lecture = mapper.mapToEntity(lectureDto);
        lecture.setEvent(persistedEvent);
        if (lectureDto.getStatus().equals(LectureStatus.REQUEST) || lectureDto.getStatus().equals(LectureStatus.REJECTED)) {
            throw new InvalidOperationException("Created lecture can't be REQUEST or REJECTED");
        }
        if (lectureDto.getStatus().equals(LectureStatus.FREE)) {
            if (Objects.nonNull(lectureDto.getSpeaker())) {
                throw new InvalidOperationException("Speaker should be null for free lecture");
            }
            Integer existedEqualsFreeLectures = lectureRepository.existsFreeLecture(persistedEvent, lecture.getTopic());
            if (existedEqualsFreeLectures > 0) {
                throw new InvalidOperationException("Lecture already exist");
            }
        } else {
            User persistedSpeaker = userRepository.getById(lectureDto.getSpeaker().getId());
            lecture.setSpeaker(persistedSpeaker);
        }
        lectureDto = mapper.mapToDto(lectureRepository.save(lecture));
        eventRepository.updateQuantityOfLectures(persistedEvent);
        return lectureDto;
    }

    @Override
    public LectureDto getById(Long id) {
        if (!lectureRepository.existsById(id)) {
            throw new EntityNotFoundException("Lecture with id:" + id + " doesn't exist");
        }
        return mapper.mapToDto(lectureRepository.getById(id));
    }

    @Override
    @Transactional
    public LectureDto assignSpeakerForFreeLecture(Long speakerId, Long lectureId) {
        if (!userRepository.existsById(speakerId)) {
            throw new EntityNotFoundException("User with id:" + speakerId + " doesn't exists");
        }
        User persistedSpeaker = userRepository.getById(speakerId);
        Lecture persistedLecture = mapper.mapToEntity(getById(lectureId));
        if (!persistedLecture.getStatus().equals(LectureStatus.FREE)) {
            throw new InvalidOperationException("Lecture with id:" + lectureId + " is not free to choose");
        }

        List<Long> appliedFreeLecturesIds = requestManagementRepository.appliedFreeLectures(persistedSpeaker.getId());
        if (!appliedFreeLecturesIds.contains(persistedLecture.getId())) {
            throw new InvalidOperationException("User with id:" + speakerId + " didn't apply free lecture with id:" + lectureId);
        }
        requestManagementRepository.assignSpeakerOnFreeLecture(persistedSpeaker.getId(), persistedLecture.getId());

        persistedLecture.setSpeaker(persistedSpeaker);
        persistedLecture.setStatus(LectureStatus.SECURED);

        lectureRepository.save(persistedLecture);
        return mapper.mapToDto(persistedLecture);
    }

    @Override
    public LectureDto rejectRequest(Long requestId) {
        Lecture persistedLecture = checkThenGet(requestId, LectureStatus.REQUEST);
        persistedLecture.setStatus(LectureStatus.REJECTED);
        return mapper.mapToDto(lectureRepository.save(persistedLecture));
    }

    @Override
    public LectureDto acceptRequest(Long requestId) {
        Lecture persistedLecture = checkThenGet(requestId, LectureStatus.REQUEST);
        persistedLecture.setStatus(LectureStatus.SECURED);
        return mapper.mapToDto(lectureRepository.save(persistedLecture));
    }

    @Override
    public List<LectureDto> moderHistory() {
        session.isLogged();
        List<LectureDto> rejectedLectures = lectureRepository.findAllByStatus(LectureStatus.REJECTED).stream()
                .peek(l -> l.setInfo("request/offer")).map(mapper::mapToDto).collect(Collectors.toList());

        List<LectureDto> rejectedFreeLectures = lectureRepository.freeLecturesByRequestStatus(RequestStatus.REJECTED).stream()
                .peek(l -> l.setInfo("free lecture")).map(mapper::mapToDto).collect(Collectors.toList());

        rejectedLectures.addAll(rejectedFreeLectures);
        return rejectedLectures;
    }

    @Override
    public List<LectureDto> getFreeLectures() {
        session.isLogged();
        User user = session.getUser();
        List<Lecture> allFreeLectures = lectureRepository.findAllByStatus(LectureStatus.FREE);
        List<Long> idsOfAppliedLectures = requestManagementRepository.appliedFreeLectures(user.getId());
        return allFreeLectures.stream().peek(l -> {
            boolean applied = idsOfAppliedLectures.contains(l.getId());
            if (applied) {
                l.setInfo("applied");
            } else {
                l.setInfo("not applied");
            }
        }).map(mapper::mapToDto).collect(Collectors.toList());

    }

    @Override
    public List<LectureDto> getLectures(LectureStatus status, Boolean isModerAccess) {
        session.isLogged();
        User sessionUser = session.getUser();
        if (isModerAccess) {
            return lectureRepository.findAllByStatus(status)
                    .stream().map(mapper::mapToDto).collect(Collectors.toList());
        } else {
            return lectureRepository.findAllByStatusAndSpeaker(status, sessionUser)
                    .stream().map(mapper::mapToDto).collect(Collectors.toList());
        }
    }

    @Override
    public LectureDto acceptOffer(Long lectureId) {
        session.isLogged();
        User sessionUser = session.getUser();
        Lecture persistedLecture = checkThenGet(lectureId, LectureStatus.OFFER, sessionUser.getId());
        persistedLecture.setStatus(LectureStatus.SECURED);
        return mapper.mapToDto(lectureRepository.save(persistedLecture));
    }

    @Override
    public LectureDto declineOffer(Long lectureId) {
        session.isLogged();
        User sessionUser = session.getUser();
        Lecture persistedLecture = checkThenGet(lectureId, LectureStatus.OFFER, sessionUser.getId());
        persistedLecture.setStatus(LectureStatus.REJECTED);
        return mapper.mapToDto(lectureRepository.save(persistedLecture));
    }

    @Override
    public LectureDto addRequest(LectureDto lectureDto) {
        session.isLogged();
        Event persistedEvent = checkThenGet(lectureDto.getEvent().getId());
        User sessionUser = session.getUser();
        lectureDto.setStatus(LectureStatus.REQUEST);
        lectureDto.setSpeaker(sessionUser);
        lectureDto.setEvent(persistedEvent);
        return mapper.mapToDto(lectureRepository.save(mapper.mapToEntity(lectureDto)));
    }

    @Override
    public LectureDto applyFreeLecture(Long lectureId) {
        session.isLogged();
        Lecture persistedLecture = checkThenGet(lectureId, LectureStatus.FREE);
        Long speakerId = session.getUser().getId();
        requestManagementRepository.applyOnFreeLecture(speakerId, lectureId);
        return mapper.mapToDto(persistedLecture);
    }

    @Override
    public List<LectureDto> speakerHistory() {
        session.isLogged();
        User sessionUser = session.getUser();
        List<LectureDto> rejectedLectures = lectureRepository.findAllByStatusAndSpeaker(LectureStatus.REJECTED, sessionUser).stream()
                .peek(l -> l.setInfo("request/offer")).map(mapper::mapToDto).collect(Collectors.toList());

        List<LectureDto> rejectedFreeLectures = lectureRepository.freeLecturesByRequestStatusAndSpeaker(RequestStatus.REJECTED, sessionUser).stream()
                .peek(l -> l.setInfo("free lecture")).map(mapper::mapToDto).collect(Collectors.toList());

        rejectedLectures.addAll(rejectedFreeLectures);
        return rejectedLectures;
    }

    private Lecture checkThenGet(Long lectureId, LectureStatus status, Long speakerId) {
        Lecture persistedLecture = checkThenGet(lectureId, status);
        if (!persistedLecture.getSpeaker().getId().equals(speakerId)) {
            throw new InvalidOperationException("User haven't access to this lecture");
        }
        return persistedLecture;
    }

    private Lecture checkThenGet(Long lectureId, LectureStatus status) {
        if (!lectureRepository.existsById(lectureId)) {
            throw new EntityNotFoundException("Lecture with id:" + lectureId + " doesn't exist");
        }
        Lecture persistedLecture = lectureRepository.getById(lectureId);
        if (!persistedLecture.getStatus().equals(status)) {
            throw new InvalidOperationException("Lecture must be with status:" + status);
        }
        return persistedLecture;
    }

    private Event checkThenGet(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EntityNotFoundException("Event with id:" + eventId + " doesn't exist");
        }
        return eventRepository.getById(eventId);
    }
}
