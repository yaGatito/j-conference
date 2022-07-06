package com.epam.jconference.service;

import com.epam.jconference.dto.LectureDto;
import com.epam.jconference.model.Lecture;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LectureService {
    //-----MODER SECTION-------
    LectureDto create(LectureDto lecture);

    LectureDto getById(Long id);

    LectureDto assignSpeakerForFreeLecture(Long speakerId, Long lectureId);

    LectureDto rejectRequest(Long requestId);

    LectureDto acceptRequest(Long requestId);

    List<LectureDto> moderHistory();

    //MIXED ACCESS
    List<LectureDto> getFreeLectures();

    //SPEAKER SECTION
    List<LectureDto> getSecuredLectures();

    List<LectureDto> getOffers();

    LectureDto acceptOffer(Long lectureId);

    LectureDto declineOffer(Long lectureId);

    List<LectureDto> getRequests();

    LectureDto addRequest(LectureDto lectureDto);

    LectureDto applyFreeLecture(Long lectureId);

    List<LectureDto> speakerHistory();
}
