package com.epam.jconference.service;

import com.epam.jconference.dto.LectureDto;
import com.epam.jconference.model.enums.LectureStatus;

import java.util.List;

public interface LectureService {
    LectureDto create(LectureDto lecture);

    LectureDto getById(Long id);

    LectureDto assignSpeakerForFreeLecture(Long speakerId, Long lectureId);

    LectureDto rejectRequest(Long requestId);

    LectureDto acceptRequest(Long requestId);

    List<LectureDto> moderHistory();

    List<LectureDto> getLectures(LectureStatus status, Boolean isModerAccess);

    LectureDto acceptOffer(Long lectureId);

    LectureDto rejectOffer(Long lectureId);

    LectureDto addRequest(LectureDto lectureDto);

    LectureDto applyFreeLecture(Long lectureId);

    List<LectureDto> speakerHistory();
}
