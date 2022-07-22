package com.epam.jconference.test.utils;

import com.epam.jconference.dto.EventDto;
import com.epam.jconference.dto.LectureDto;
import com.epam.jconference.dto.UserDto;
import com.epam.jconference.model.Event;
import com.epam.jconference.model.Lecture;
import com.epam.jconference.model.User;
import com.epam.jconference.model.enums.LectureStatus;

public class TestLectureDataUtil {

    public static final Long ID = 1L;
    public static final String TOPIC = "Java Collections Framework";
    public static final LectureStatus STATUS = LectureStatus.SECURED;
    public static final EventDto EVENT_DTO = EventDto.builder().id(ID).build();
    public static final UserDto SPEAKER_DTO = UserDto.builder().id(ID).build();
    public static final User SPEAKER = TestUserDataUtil.createUser();
    public static final Event EVENT = TestEventDataUtil.createEvent();

    public static LectureDto createLectureDto() {
        return LectureDto.builder()
                         .topic(TOPIC)
                         .status(STATUS)
                         .speaker(SPEAKER_DTO)
                         .event(EVENT_DTO)
                         .build();
    }

    public static Lecture createLecture() {
        return Lecture.builder()
                      .id(ID)
                      .topic(TOPIC)
                      .status(STATUS)
                      .speaker(SPEAKER)
                      .event(EVENT)
                      .build();
    }
}
