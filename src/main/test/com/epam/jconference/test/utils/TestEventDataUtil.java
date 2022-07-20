package com.epam.jconference.test.utils;

import com.epam.jconference.dto.EventDto;
import com.epam.jconference.dto.TagDto;
import com.epam.jconference.model.Event;
import com.epam.jconference.model.Tag;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TestEventDataUtil {
    public static final Long ID = 1L;
    public static final String TOPIC = "Java SE";
    public static final String LOCATION = "m. Arnautskaya str., 9B";
    public static final LocalTime START_TIME = LocalTime.of(12, 10);
    public static final LocalTime END_TIME = LocalTime.of(15, 10);
    public static final LocalDate DATE = LocalDate.of(2022, 9, 1);
    public static final List<Tag> TAGS = List.of(TestTagDataUtil.createTag1(), TestTagDataUtil.createTag2());
    public static final List<TagDto> TAGS_DTO = List.of(TestTagDataUtil.createTag1Dto(),
            TestTagDataUtil.createTag2Dto());
    public static final Integer LECTURES = 4;
    public static final Integer LISTENERS = 5;

    public static Event createEvent() {
        return Event.builder()
                    .id(ID)
                    .topic(TOPIC)
                    .location(LOCATION)
                    .startTime(START_TIME)
                    .endTime(END_TIME)
                    .date(DATE)
                    .tags(TAGS)
                    .lectures(LECTURES)
                    .listeners(LISTENERS)
                    .build();
    }

    public static EventDto createEventDto() {
        return EventDto.builder()
                       .topic(TOPIC)
                       .location(LOCATION)
                       .startTime(START_TIME)
                       .endTime(END_TIME)
                       .date(DATE)
                       .tags(TAGS_DTO)
                       .build();
    }
}
