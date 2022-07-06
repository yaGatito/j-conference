package com.epam.jconference.repository.impl;

import com.epam.jconference.bean.Session;
import com.epam.jconference.exception.EntityNotFoundException;
import com.epam.jconference.exception.InvalidOperationException;
import com.epam.jconference.exception.UnauthorizedAccessException;
import com.epam.jconference.model.Lecture;
import com.epam.jconference.model.User;
import com.epam.jconference.model.enums.LectureStatus;
import com.epam.jconference.model.enums.UserRole;
import com.epam.jconference.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepository {

    private final List<Lecture> lectures = new ArrayList<>();
    // indexes: 0 - lecture, 1 - speaker, 2 - status
    // status: -1 - rejected, 0 - pending, 1 - selected
    private final List<Long[]> requestsOnFreeLectures = new ArrayList<>();
    private final Session session;

    @Override
    public Lecture create(Lecture lecture) {
        lectures.add(lecture);
        int id = lectures.indexOf(lecture);
        lecture.setId((long) id);
        if (lecture.getStatus().equals(LectureStatus.FREE_TO_CHOOSE)) {
            lecture.setSpeaker(null);
        }
        return lecture;
    }

    @Override
    public Lecture getById(Long id) {
        if (id >= lectures.size() || id < 0) {
            throw new EntityNotFoundException("Invalid ID of lecture: " + id);
        }
        return lectures.get(id.intValue());
    }

    @Override
    public Lecture assignSpeakerForFreeLecture(Long speakerId, Long lectureId) {
        Lecture lecture = getById(lectureId);

        requestsOnFreeLectures.stream().filter(arr -> arr[0].equals(lectureId) && arr[1].equals(speakerId)).findAny().orElseThrow(() -> new EntityNotFoundException("This request doesn't exist"));

        requestsOnFreeLectures.stream().filter(arr -> arr[0].equals(lectureId)).forEach(arr -> {
            if (arr[1].equals(speakerId)) {
                arr[2] = 1L;
            } else {
                arr[2] = -1L;
            }
        });
        lecture.setSpeaker(speakerId);
        return lecture;
    }

    @Override
    public Lecture rejectRequest(Long requestId) {
        Lecture lecture = getById(requestId);
        User user = session.getUser();
        if (user == null || !lecture.getStatus().equals(LectureStatus.REQUEST)) {
            throw new InvalidOperationException("User in null: " + (user == null) + ";" + " Mismatch lectures status: " + !lecture.getStatus().equals(LectureStatus.REQUEST));
        }
        lecture.setStatus(LectureStatus.REJECTED);
        return lecture;
    }

    @Override
    public Lecture acceptRequest(Long requestId) {
        Lecture lecture = getById(requestId);
        User user = session.getUser();
        if (user == null || !lecture.getStatus().equals(LectureStatus.REQUEST)) {
            throw new InvalidOperationException("User in null: " + (user == null) + ";" + " Mismatch lectures status: " + !lecture.getStatus().equals(LectureStatus.REQUEST));
        }
        lecture.setStatus(LectureStatus.SECURED);
        return lecture;
    }

    @Override
    public List<Lecture> moderHistory() {
        return lectures.stream().filter(lecture -> lecture.getStatus().equals(LectureStatus.REJECTED) || lecture.getStatus().equals(LectureStatus.REQUEST)).collect(Collectors.toList());
    }

    @Override
    public List<Lecture> getFreeLectures() {
        return lectures.stream().filter(lecture -> lecture.getStatus().equals(LectureStatus.FREE_TO_CHOOSE)).collect(Collectors.toList());
    }

    @Override
    public List<Lecture> getSecuredLectures() {
        User currentSpeaker = session.getUser();
        if (currentSpeaker == null) {
            throw new UnauthorizedAccessException("User is unauthorized");
        }
        return lectures.stream().filter(l -> l.getSpeaker().equals(currentSpeaker.getId())).collect(Collectors.toList());
    }

    @Override
    public List<Lecture> getOffers() {
        User currentSpeaker = session.getUser();
        if (currentSpeaker == null) {
            throw new UnauthorizedAccessException("User is unauthorized");
        }
        return lectures.stream().filter(l -> l.getSpeaker().equals(currentSpeaker.getId()) && l.getStatus().equals(LectureStatus.OFFER)).collect(Collectors.toList());
    }

    @Override
    public Lecture acceptOffer(Long lectureId) {
        Lecture lecture = getById(lectureId);
        User user = session.getUser();
        if (user == null || !lecture.getSpeaker().equals(user.getId()) || !lecture.getStatus().equals(LectureStatus.OFFER)) {
            throw new InvalidOperationException("User in null: " + (user == null) + ";" + " Mismatch speakers: " + (user == null || !lecture.getSpeaker().equals(user.getId())) + ";" + " Mismatch lectures status: " + !lecture.getStatus().equals(LectureStatus.OFFER));
        }
        lecture.setStatus(LectureStatus.SECURED);
        return lecture;
    }

    @Override
    public Lecture declineOffer(Long lectureId) {
        Lecture lecture = getById(lectureId);
        User user = session.getUser();
        if (user == null || !lecture.getSpeaker().equals(user.getId()) || !lecture.getStatus().equals(LectureStatus.OFFER)) {
            throw new InvalidOperationException("User in null: " + (user == null) + ";" + " Mismatch speakers: " + (user == null || !lecture.getSpeaker().equals(user.getId())) + ";" + " Mismatch lectures status: " + !lecture.getStatus().equals(LectureStatus.OFFER));
        }
        lecture.setStatus(LectureStatus.REJECTED);
        return lecture;
    }

    @Override
    public List<Lecture> getRequests() {
        Long speakerId = session.getUser().getId();
        return lectures.stream().filter(l -> l.getSpeaker().equals(speakerId) && l.getStatus().equals(LectureStatus.REQUEST)).collect(Collectors.toList());
    }

    @Override
    public Lecture addRequest(Lecture lecture) {
        User user = session.getUser();
        if (user == null) {
            throw new UnauthorizedAccessException("User is unauthorized");
        }
        lecture.setStatus(LectureStatus.REQUEST);
        lecture.setSpeaker(user.getId());
        return create(lecture);
    }

    @Override
    public Lecture applyFreeLecture(Long lectureId) {
        User user = session.getUser();
        if (user == null) {
            throw new UnauthorizedAccessException("User is unauthorized");
        }
        Lecture lecture = getById(lectureId);
        if (!lecture.getStatus().equals(LectureStatus.FREE_TO_CHOOSE)) {
            throw new InvalidOperationException("This lecture is not free to choose");
        }
        Long[] arr = new Long[3];
        arr[0] = lectureId;
        arr[1] = user.getId();
        arr[2] = 0L;
        requestsOnFreeLectures.add(arr);
        return lectures.get(lectureId.intValue());
    }

    @Override
    public List<Lecture> speakerHistory() {
        User user = session.getUser();
        if (user == null) {
            throw new UnauthorizedAccessException("User is unauthorized");
        }

        List<Lecture> collect = requestsOnFreeLectures.stream().filter(arr -> arr[1].equals(user.getId()) && arr[2].equals(-1L)).map(arr -> getById(arr[0])).collect(Collectors.toList());

        collect.addAll(lectures.stream().filter(l -> l.getStatus().equals(LectureStatus.REJECTED) && l.getSpeaker().equals(user.getId())).peek(l -> l.setStatus(LectureStatus.REJECTED)).collect(Collectors.toList()));

        return collect;
    }

    public Map<String, Object> infoContribution() {
        Map<String, Object> info = new HashMap<>();
        info.put(LectureStatus.OFFER.name(), 0);
        info.put(LectureStatus.REQUEST.name(), 0);
        info.put(LectureStatus.REJECTED.name(), 0);
        info.put(LectureStatus.FREE_TO_CHOOSE.name(), 0);
        info.put(LectureStatus.SECURED.name(), 0);

        lectures.forEach(lecture -> {
            String status = lecture.getStatus().name();
            info.put(status, (Integer) info.get(status) + 1);
        });

        return info;
    }
}
