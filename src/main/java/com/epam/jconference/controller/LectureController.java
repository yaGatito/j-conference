package com.epam.jconference.controller;

import com.epam.jconference.api.LectureApi;
import com.epam.jconference.dto.LectureDto;
import com.epam.jconference.service.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LectureController implements LectureApi {

    private final LectureService lectureService;

    //MODER SECTION
    public LectureDto create(LectureDto lectureDto) {
        return lectureService.create(lectureDto);
    }

    public LectureDto findById(Long id) {
        return lectureService.getById(id);
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

    //MIXED ACCESS
    public List<LectureDto> getFreeLectures() {
        return lectureService.getFreeLectures();
    }

    //SPEAKER SECTION
    public List<LectureDto> getSecuredLectures() {
        return lectureService.getSecuredLectures();
    }

    public List<LectureDto> getOffers() {
        return lectureService.getOffers();
    }

    public LectureDto acceptOffer(Long lectureId) {
        return lectureService.acceptOffer(lectureId);
    }

    public LectureDto declineOffer(Long lectureId) {
        return lectureService.declineOffer(lectureId);
    }

    public List<LectureDto> getRequests() {
        return lectureService.getRequests();
    }

    public LectureDto addRequest(LectureDto lectureDto) {
        return lectureService.addRequest(lectureDto);
    }

    public LectureDto applyFreeLecture(Long lectureId) {
        return lectureService.applyFreeLecture(lectureId);
    }

    public List<LectureDto> speakerHistory() {
        return lectureService.speakerHistory();
    }
}
