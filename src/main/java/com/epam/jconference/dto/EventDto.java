package com.epam.jconference.dto;

import com.epam.jconference.dto.group.OnCreate;
import com.epam.jconference.dto.group.OnUpdate;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Null(message = "{event.topic.null}", groups = OnUpdate.class)
    @NotBlank(message = "{event.topic.not_blank}", groups = OnCreate.class)
    private String topic;

    @NotEmpty(message = "{event.tags.not_empty}", groups = OnCreate.class)
    private List<Long> tags;

    @NotNull(message = "{event.start_time.not_null}", groups = OnCreate.class)
    private LocalTime startTime;

    @NotNull(message = "{event.end_time.not_null}", groups = OnCreate.class)
    private LocalTime endTime;

    @Future(message = "{event.date.future}", groups = {OnCreate.class, OnUpdate.class})
    private LocalDate date;

    @NotBlank(message = "{event.location.not_blank}", groups = OnCreate.class)
    private String location;

    @Null(message = "{event.lectures.null}", groups = {OnCreate.class, OnUpdate.class})
    private List<Long> lectures;

    @Null(message = "{event.listeners.null}", groups = {OnCreate.class, OnUpdate.class})
    private List<Long> listeners;
}
