package com.epam.jconference.controller.dto;

import com.epam.jconference.service.model.Event;
import com.epam.jconference.service.model.enums.LectureStatus;
import com.epam.jconference.service.model.User;
import lombok.Data;

@Data
public class LectureDto {
    private Long id;
    private String topic;
    private LectureStatus status;
    private Event event;
    private User speaker;
}