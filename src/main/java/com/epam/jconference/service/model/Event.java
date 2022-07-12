package com.epam.jconference.service.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class Event {
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
