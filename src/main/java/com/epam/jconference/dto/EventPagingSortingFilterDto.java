package com.epam.jconference.dto;

import com.epam.jconference.model.enums.SortEventOption;
import lombok.Data;

@Data
public class EventPagingSortingFilterDto {
    public final Integer NUMBER_OF_ELEMENTS = 5;
    private Boolean descendingOrder = true;
    private Boolean futureEvents = true;
    private Boolean pastEvents = false;
    private SortEventOption option = SortEventOption.ID;
    private Integer page = 0;
}
