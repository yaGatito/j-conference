package com.epam.jconference.dto;

import com.epam.jconference.model.enums.SortEventOption;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NonNull;

@Data
public class EventFilterDto {
    private Boolean descendingOrder = true;
    private Boolean futureEvents = true;
    private Boolean pastEvents = false;
    private SortEventOption option = SortEventOption.DEFAULT;
}
