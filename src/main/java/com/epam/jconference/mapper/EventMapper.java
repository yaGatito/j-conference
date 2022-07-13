package com.epam.jconference.mapper;

import com.epam.jconference.model.Event;
import com.epam.jconference.dto.EventDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    Event mapToEntity(EventDto eventDto);

    EventDto mapToDto(Event event);
}
