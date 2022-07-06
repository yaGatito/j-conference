package com.epam.jconference.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Data
public class Event {
    private Long id;
    private String topic;
    private List<Long> tags;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private List<Long> lectures;
    private List<Long> listeners;

    /**
     * Returns an array. First element of which contains only past events.
     * Second element of which contains today and future events.
     *
     * @param events list which will be filtered to past and future
     * @return array
     */
    public static List<Event>[] filter(List<Event> events) {
        List<Event> past = new ArrayList<>();
        List<Event> future = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalTime timeNow = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute());
        for (Event event : events) {
            if (event.getDate().compareTo(today) > 0 || (event.getDate().compareTo(today) == 0 && event.getStartTime().compareTo(timeNow) >= 0)) {
                future.add(event);
            } else {
                past.add(event);
            }
        }
        return new List[]{past, future};
    }
}
