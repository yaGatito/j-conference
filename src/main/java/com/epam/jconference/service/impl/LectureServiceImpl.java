package com.epam.jconference.service.impl;

import com.epam.jconference.dto.LectureDto;
import com.epam.jconference.mapper.LectureMapper;
import com.epam.jconference.repository.LectureRepository;
import com.epam.jconference.service.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;

    @Override
    public LectureDto create(LectureDto lecture) {
        return LectureMapper.INSTANCE.mapToDto(lectureRepository.create(LectureMapper.INSTANCE.mapToEntity(lecture)));
    }

    @Override
    public LectureDto getById(Long id) {
        return LectureMapper.INSTANCE.mapToDto(lectureRepository.getById(id));
    }

    @Override
    public LectureDto assignSpeakerForFreeLecture(Long speakerId, Long lectureId) {
        return LectureMapper.INSTANCE.mapToDto(lectureRepository.assignSpeakerForFreeLecture(speakerId, lectureId));
    }

    @Override
    public LectureDto rejectRequest(Long requestId) {
        return LectureMapper.INSTANCE.mapToDto(lectureRepository.rejectRequest(requestId));
    }

    @Override
    public LectureDto acceptRequest(Long requestId) {
        return LectureMapper.INSTANCE.mapToDto(lectureRepository.acceptRequest(requestId));
    }

    @Override
    public List<LectureDto> moderHistory() {
        return lectureRepository.moderHistory().stream().map(LectureMapper.INSTANCE::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<LectureDto> getFreeLectures() {
        return lectureRepository.getFreeLectures().stream().map(LectureMapper.INSTANCE::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<LectureDto> getSecuredLectures() {
        return lectureRepository.getSecuredLectures().stream().map(LectureMapper.INSTANCE::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<LectureDto> getOffers() {
        return lectureRepository.getOffers().stream().map(LectureMapper.INSTANCE::mapToDto).collect(Collectors.toList());
    }

    @Override
    public LectureDto acceptOffer(Long lectureId) {
        return LectureMapper.INSTANCE.mapToDto(lectureRepository.acceptOffer(lectureId));
    }

    @Override
    public LectureDto declineOffer(Long lectureId) {
        return LectureMapper.INSTANCE.mapToDto(lectureRepository.declineOffer(lectureId));
    }

    @Override
    public List<LectureDto> getRequests() {
        return lectureRepository.getRequests().stream().map(LectureMapper.INSTANCE::mapToDto).collect(Collectors.toList());
    }

    @Override
    public LectureDto addRequest(LectureDto lectureDto) {
        return LectureMapper.INSTANCE.mapToDto(lectureRepository.addRequest(LectureMapper.INSTANCE.mapToEntity(lectureDto)));
    }

    @Override
    public LectureDto applyFreeLecture(Long lectureId) {
        return LectureMapper.INSTANCE.mapToDto(lectureRepository.applyFreeLecture(lectureId));
    }

    @Override
    public List<LectureDto> speakerHistory() {
        return lectureRepository.speakerHistory().stream().map(LectureMapper.INSTANCE::mapToDto).collect(Collectors.toList());
    }
}
