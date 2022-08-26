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

    @NotNull(message = "{id}{not_null}", groups = OnUpdate.class)
    @Null(message = "{id}{null}", groups = OnCreate.class)
    private Long id;

    @Null(message = "{topic}{null}", groups = OnUpdate.class)
    @NotBlank(message = "{topic}{not_blank}", groups = OnCreate.class)
    @ValidateString(message = "{topic}{invalid}", value = StringItem.TOPIC, groups = {OnCreate.class})
    private String topic;

    @NotEmpty(message = "{tags}{not_empty}", groups = OnCreate.class)
    private List<TagDto> tags;

    @NotNull(message = "{start_time}{not_null}", groups = OnCreate.class)
    private LocalTime startTime;

    @NotNull(message = "{end_time}{not_null}", groups = OnCreate.class)
    private LocalTime endTime;

    @Future(message = "{date}{future}", groups = {OnCreate.class, OnUpdate.class})
    @NotNull(message = "{end_time}{not_null}", groups = OnCreate.class)
    private LocalDate date;

    @NotBlank(message = "{location}{not_blank}", groups = OnCreate.class)
    @ValidateString(message = "{location}{invalid}", value = StringItem.LOCATION, groups = {OnCreate.class, OnUpdate.class})
    private String location;

    @Null(message = "{listeners}{null}", groups = {OnCreate.class, OnUpdate.class})
    private Integer listeners;

    @Null(message = "{lectures}{null}", groups = {OnCreate.class, OnUpdate.class})
    private Integer lectures;
}
