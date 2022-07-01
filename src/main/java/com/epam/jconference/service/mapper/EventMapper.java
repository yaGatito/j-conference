package com.epam.jconference.service.mapper;

import com.epam.jconference.service.model.Event;
import com.epam.jconference.controller.dto.EventDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    Event mapToEntity(EventDto eventDto);

    EventDto mapToDto(Event event);
}
