package com.epam.jconference.controller.dto;

import com.epam.jconference.service.model.Lecture;
import com.epam.jconference.service.model.Location;
import com.epam.jconference.service.model.Tag;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class EventDto {
    private Long id;
    private String topic;
    private List<Tag> tags;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private Boolean isOnline;
    private Location location;
    private List<Lecture> lectures;
    private Integer listeners;
}
