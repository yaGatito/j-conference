package com.epam.jconference.controller.dto;

import com.epam.jconference.service.model.enums.SortEventOption;

public class EventFilterDto {
    private Boolean descendingOrder;
    private Boolean futureEvents;
    private Boolean pastEvents;
    private SortEventOption option;
}
