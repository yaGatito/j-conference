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
    public List<LectureDto> getSecuredLecturesForModer() {
        return lectureService.getLectures(LectureStatus.SECURED, true);
    }

    @Override
    public List<LectureDto> getFreeLecturesForModer() {
        return lectureService.getLectures(LectureStatus.FREE, true);
    }

    public LectureDto assignSpeakerForFreeLecture(Long speakerId, Long lectureId) {
        return lectureService.assignSpeakerForFreeLecture(speakerId, lectureId);
    }

    @Override
    public List<LectureDto> getRequestsForModer() {
        return lectureService.getLectures(LectureStatus.REQUEST, true);
    }

    public LectureDto rejectRequest(Long requestId) {
        return lectureService.rejectRequest(requestId);
    }

    public LectureDto acceptRequest(Long requestId) {
        return lectureService.acceptRequest(requestId);
    }

    @Override
    public List<LectureDto> getOffersForModer() {
        return lectureService.getLectures(LectureStatus.OFFER, true);
    }

    public List<LectureDto> moderHistory() {
        return lectureService.moderHistory();
    }

    public List<LectureDto> getSecuredLectures() {
        return lectureService.getLectures(LectureStatus.SECURED, false);
    }

    public List<LectureDto> getFreeLectures() {
        return lectureService.getFreeLectures();
    }

    public LectureDto applyFreeLecture(Long lectureId) {
        return lectureService.applyFreeLecture(lectureId);
    }

    public List<LectureDto> getOffers() {
        return lectureService.getLectures(LectureStatus.OFFER, false);
    }

    public LectureDto acceptOffer(Long lectureId) {
        return lectureService.acceptOffer(lectureId);
    }

    public LectureDto rejectOffer(Long lectureId) {
        return lectureService.declineOffer(lectureId);
    }

    public List<LectureDto> getRequests() {
        return lectureService.getLectures(LectureStatus.REQUEST, false);
    }

    public LectureDto addRequest(LectureDto lectureDto) {
        return lectureService.addRequest(lectureDto);
    }

    public List<LectureDto> speakerHistory() {
        return lectureService.speakerHistory();
    }
}
