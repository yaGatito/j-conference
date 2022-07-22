package com.epam.jconference.controller;

import com.epam.jconference.api.LectureApi;
import com.epam.jconference.dto.LectureDto;
import com.epam.jconference.model.enums.LectureStatus;
import com.epam.jconference.service.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LectureController implements LectureApi {

    private final LectureService lectureService;

    public LectureDto create(LectureDto lectureDto) {
        return lectureService.create(lectureDto);
    }

    public LectureDto findById(Long id) {
        return lectureService.getById(id);
    }

    @Override
    public List<LectureDto> getLecturesForModer(String status) {
        return lectureService.getLectures(LectureStatus.valueOf(status.toUpperCase()), true);
    }

    public LectureDto assignSpeakerForFreeLecture(Long speakerId, Long lectureId) {
        return lectureService.assignSpeakerForFreeLecture(speakerId, lectureId);
    }

    public LectureDto rejectRequest(Long requestId) {
        return lectureService.rejectRequest(requestId);
    }

    public LectureDto acceptRequest(Long requestId) {
        return lectureService.acceptRequest(requestId);
    }

    public List<LectureDto> moderHistory() {
        return lectureService.moderHistory();
    }

    @Override
    public List<LectureDto> getLecturesForSpeaker(String status) {
        return lectureService.getLectures(LectureStatus.valueOf(status.toUpperCase()), false);
    }

    public LectureDto applyFreeLecture(Long lectureId) {
        return lectureService.applyFreeLecture(lectureId);
    }

    public LectureDto acceptOffer(Long lectureId) {
        return lectureService.acceptOffer(lectureId);
    }

    public LectureDto rejectOffer(Long lectureId) {
        return lectureService.rejectOffer(lectureId);
    }

    public LectureDto addRequest(LectureDto lectureDto) {
        return lectureService.addRequest(lectureDto);
    }

    public List<LectureDto> speakerHistory() {
        return lectureService.speakerHistory();
    }
}
