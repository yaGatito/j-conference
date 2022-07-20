package com.epam.jconference.dto;

import com.epam.jconference.dto.group.OnCreate;
import com.epam.jconference.dto.group.OnUpdate;
import com.epam.jconference.dto.validation.strings.StringItem;
import com.epam.jconference.dto.validation.strings.ValidateString;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder(access = AccessLevel.PUBLIC)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDto {

    @NotNull(groups = OnUpdate.class)
    @Null(groups = OnCreate.class)
    private Long id;

    @Null(message = "{event.topic.null}", groups = OnUpdate.class)
    @NotBlank(groups = OnCreate.class)
    @ValidateString(value = StringItem.TOPIC, groups = {OnCreate.class})
    private String topic;

    @NotEmpty(message = "{event.tags.not_empty}", groups = OnCreate.class)
    private List<TagDto> tags;

    @NotNull(message = "{event.start_time.not_null}", groups = OnCreate.class)
    private LocalTime startTime;

    @NotNull(message = "{event.end_time.not_null}", groups = OnCreate.class)
    private LocalTime endTime;

    @Future(message = "{event.date.future}", groups = {OnCreate.class, OnUpdate.class})
    @NotNull(message = "{event.end_time.not_null}", groups = OnCreate.class)
    private LocalDate date;

    @NotBlank(message = "{event.location.not_blank}", groups = OnCreate.class)
    @ValidateString(value = StringItem.LOCATION, groups = {OnCreate.class, OnUpdate.class})
    private String location;

    @Null(message = "{event.listeners.null}", groups = {OnCreate.class, OnUpdate.class})
    private Integer listeners;

    @Null(message = "{event.lectures.null}", groups = {OnCreate.class, OnUpdate.class})
    private Integer lectures;
}
