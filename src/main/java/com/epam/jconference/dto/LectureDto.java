package com.epam.jconference.dto;

import com.epam.jconference.dto.group.OnCreate;
import com.epam.jconference.dto.group.OnRequest;
import com.epam.jconference.dto.validation.strings.StringItem;
import com.epam.jconference.dto.validation.strings.ValidateString;
import com.epam.jconference.model.enums.LectureStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LectureDto {
    @Null(message = "id must be null", groups = {OnCreate.class, OnRequest.class})
    private Long id;

    @NotBlank(message = "{lectures.topic.not_blank}", groups = {OnRequest.class, OnCreate.class})
    @ValidateString(value = StringItem.TOPIC, groups = {OnRequest.class, OnCreate.class})
    private String topic;

    @NotNull(message = "{lectures.status.not_null}", groups = OnCreate.class)
    @Null(message = "{lectures.status.null}", groups = OnRequest.class)
    private LectureStatus status;

    @NotNull(message = "{lectures.event.not_null}", groups = {OnRequest.class, OnCreate.class})
    private EventDto event;

    @NotNull(message = "{lectures.event.not_null}", groups = OnCreate.class)
    @Null(message = "{lectures.speaker.null}", groups = OnRequest.class)
    private UserDto speaker;

    private String info;
}
