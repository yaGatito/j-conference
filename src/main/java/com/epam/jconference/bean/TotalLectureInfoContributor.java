package com.epam.jconference.bean;

import com.epam.jconference.model.Lecture;
import com.epam.jconference.model.enums.LectureStatus;
import com.epam.jconference.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TotalLectureInfoContributor implements InfoContributor {

    private final LectureRepository lectureRepository;

    @Override
    public void contribute(Info.Builder builder) {
        Map<Long, String> lecturesInfo = lectureRepository.findAllByStatus(LectureStatus.SECURED).stream().collect(Collectors.toMap(Lecture::getId, Lecture::getTopic));
        builder.withDetail("lectures", lecturesInfo);
    }
}
