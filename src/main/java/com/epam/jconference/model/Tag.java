package com.epam.jconference.model;

import lombok.Data;

import java.util.Map;

@Data
public class Tag {

    private Long id;
    private String name;
    private Map<String, String> translations;
}
