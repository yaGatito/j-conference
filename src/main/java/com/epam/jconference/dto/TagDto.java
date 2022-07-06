package com.epam.jconference.dto;

import com.epam.jconference.dto.validation.TagConstraint;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

import javax.validation.constraints.Null;
import java.util.Map;

@Data
public class TagDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @TagConstraint(message = "{tag.name}")
    private String name;

    @JsonUnwrapped
    @Null
    private Map<String, String> translations;
}
