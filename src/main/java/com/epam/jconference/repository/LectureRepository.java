package com.epam.jconference.repository;

import com.epam.jconference.dto.LectureDto;
import com.epam.jconference.model.Lecture;
import com.epam.jconference.model.enums.LectureStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface LectureRepository {
    //MODER SECTION
    Lecture create(Lecture lecture);

    Lecture getById(Long id);

    Lecture assignSpeakerForFreeLecture(Long speakerId, Long lectureId);

    Lecture rejectRequest(Long requestId);

    Lecture acceptRequest(Long requestId);

    List<Lecture> moderHistory();

    //MIXED ACCESS
    List<Lecture> getFreeLectures();

    //SPEAKER SECTION
    List<Lecture> getSecuredLectures();

    List<Lecture> getOffers();

    Lecture acceptOffer(Long lectureId);

    Lecture declineOffer(Long lectureId);

    List<Lecture> getRequests();

    Lecture addRequest(Lecture lectureDto);

    Lecture applyFreeLecture(Long lectureId);

    List<Lecture> speakerHistory();

    //UTIL
    Map<String, Object> infoContribution();
}
