package com.epam.jconference.service.model;

import lombok.Data;

@Data
public class Location {
    private Boolean isOnline;
    private String address;
    private String shortName;
}
