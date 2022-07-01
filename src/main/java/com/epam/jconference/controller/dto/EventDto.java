package com.epam.jconference.controller.dto;

import com.epam.jconference.service.model.Lecture;
import com.epam.jconference.service.model.Location;
import com.epam.jconference.service.model.Tag;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    /**
     * Returns an array. First element of which contains only past events.
     * Second element of which contains today and future events.
     * @param events list which will be filtered to past and future
     * @return array
     */
    public static List<EventDto>[] filter(List<EventDto> events){
        List<EventDto> past = new ArrayList<>();
        List<EventDto> future = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalTime timeNow = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute());
        for (EventDto event : events) {
            if (event.getDate().compareTo(today)>0 || (event.getDate().compareTo(today)==0 && event.getStartTime().compareTo(timeNow)>=0)){
                future.add(event);
            } else{
                past.add(event);
            }
        }
        return new List[]{past,future};
    }
}
