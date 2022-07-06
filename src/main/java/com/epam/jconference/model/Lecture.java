package com.epam.jconference.model;

import com.epam.jconference.model.enums.LectureStatus;
import lombok.Data;

@Data
public class Lecture {

    private Long id;
    private String topic;
    private LectureStatus status;
    private Long event;
    private Long speaker;
}
