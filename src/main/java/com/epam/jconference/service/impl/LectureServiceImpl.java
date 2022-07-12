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
        if (!eventRepository.existsById(lectureDto.getEvent().getId())) {
            throw new EntityNotFoundException("Event with id:" + lectureDto.getEvent().getId() + " doesn't exist");
        }
        Event persistedEvent = eventRepository.getById(lectureDto.getEvent().getId());
        Lecture lecture = mapper.mapToEntity(lectureDto);
        lecture.setEvent(persistedEvent);
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
    public LectureDto applyFreeLecture(Long lectureId) {
        session.isLogged();
        Lecture persistedLecture = mapper.mapToEntity(getById(lectureId));
        if (!persistedLecture.getStatus().equals(LectureStatus.FREE)) {
            throw new InvalidOperationException("Lecture should be free to choose");
        }
        Long speakerId = session.getUser().getId();
        requestManagementRepository.applyOnFreeLecture(speakerId, lectureId);
        return mapper.mapToDto(persistedLecture);
    }

    @Override
    public LectureDto rejectRequest(Long requestId) {
        return null;
    }

    @Override
    public List<LectureDto> getRequests() {
        return null;
    }

    @Override
    public LectureDto acceptRequest(Long requestId) {
        return null;
    }

    @Override
    public LectureDto addRequest(LectureDto lectureDto) {
        return null;
    }

    @Override
    public List<LectureDto> getSecuredLectures() {
        return null;
    }

    @Override
    public List<LectureDto> getOffers() {
        return null;
    }

    @Override
    public LectureDto acceptOffer(Long lectureId) {
        return null;
    }

    @Override
    public LectureDto declineOffer(Long lectureId) {
        return null;
    }


    @Override
    public List<LectureDto> speakerHistory() {
        return null;
    }

    @Override
    public List<LectureDto> moderHistory() {
        return null;
    }
}
