package com.epam.jconference.dto;

import com.epam.jconference.model.enums.SortEventOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventPagingSortingFilterDto {
    public final Integer NUMBER_OF_ELEMENTS = 5;
    private Boolean descendingOrder = true;
    private Boolean futureEvents = true;
    private SortEventOption option = SortEventOption.ID;
    private Integer page = 0;
}
