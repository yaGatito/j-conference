package com.epam.jconference.dto;

import com.epam.jconference.dto.group.OnCreate;
import com.epam.jconference.model.Event;
import com.epam.jconference.model.enums.LectureStatus;
import com.epam.jconference.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LectureDto {
    private Long id;

    @NotBlank(message = "{lectures.topic.not_blank}")
    private String topic;

    @NotNull(message = "{lectures.status.not_null}")
    private LectureStatus status;

    @NotNull(message = "{lectures.event.not_null}")
    private Long event;

    private Long speaker;
}
