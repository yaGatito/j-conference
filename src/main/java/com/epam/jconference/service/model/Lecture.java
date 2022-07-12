package com.epam.jconference.service.model;

import com.epam.jconference.service.model.enums.LectureStatus;
import lombok.Data;

@Data
public class Lecture {
    private Long id;
    private String topic;
    private LectureStatus status;
    private Event event;
    private User speaker;
}
