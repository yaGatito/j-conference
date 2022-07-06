package com.epam.jconference.bean;

import com.epam.jconference.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class TotalLectureInfoContributor implements InfoContributor {

    private final LectureRepository lectureRepository;

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> lecturesInfo = lectureRepository.infoContribution();
        builder.withDetail("lectures", lecturesInfo);
    }
}
