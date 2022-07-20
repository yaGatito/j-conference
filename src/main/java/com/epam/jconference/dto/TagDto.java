package com.epam.jconference.dto;

import com.epam.jconference.dto.validation.strings.StringItem;
import com.epam.jconference.dto.validation.strings.ValidateString;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

import javax.validation.constraints.Null;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDto {

    private Long id;

    @ValidateString(value = StringItem.TAG)
    private String name;

    @JsonUnwrapped
    @Null
    private Map<String, String> translations;
}
