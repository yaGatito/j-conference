package com.epam.jconference.dto;

import com.epam.jconference.dto.group.OnRequest;
import com.epam.jconference.model.Event;
import com.epam.jconference.model.User;
import com.epam.jconference.model.enums.LectureStatus;
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
    @NotBlank(message = "{lectures.topic.not_blank}",groups = OnRequest.class)
    private String topic;

    @NotNull(message = "{lectures.status.not_null}")
    @Null(message = "{lectures.status.null}",groups = OnRequest.class)
    private LectureStatus status;

    private String info;

    @NotNull(message = "{lectures.event.not_null}")
    @NotNull(message = "{lectures.event.not_null}", groups = OnRequest.class)
    private Event event;

    @Null(message = "{lectures.speaker.null}",groups = OnRequest.class)
    private User speaker;
}
