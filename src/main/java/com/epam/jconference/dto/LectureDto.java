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
    @Null(message = "{id}{null}", groups = {OnCreate.class, OnRequest.class})
    private Long id;

    @NotBlank(message = "{topic}{not_blank}", groups = {OnRequest.class, OnCreate.class})
    @ValidateString(message = "{topic}{invalid}", value = StringItem.TOPIC, groups = {OnRequest.class, OnCreate.class})
    private String topic;

    @NotNull(message = "{l_status}{not_null}", groups = OnCreate.class)
    @Null(message = "{l_status}{null}", groups = OnRequest.class)
    private LectureStatus status;

    @NotNull(message = "{event}{not_null}", groups = {OnRequest.class, OnCreate.class})
    private EventDto event;

    @NotNull(message = "{event}{not_null}", groups = OnCreate.class)
    @Null(message = "{speaker}{null}", groups = OnRequest.class)
    private UserDto speaker;

    private String info;
}
