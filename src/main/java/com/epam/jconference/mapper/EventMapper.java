package com.epam.jconference.mapper;

import com.epam.jconference.dto.EventDto;
import com.epam.jconference.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

@Mapper
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    Event mapToEntity(EventDto eventDto);

    EventDto mapToDto(Event event);

    default void updateFieldsForEvent(EventDto eventDto, Event event) {
        if (eventDto.getDate() != null) {
            event.setDate(eventDto.getDate());
        }
        if (eventDto.getStartTime() != null) {
            event.setStartTime(eventDto.getStartTime());
        }
        if (eventDto.getEndTime() != null) {
            event.setEndTime(eventDto.getEndTime());
        }
        if (eventDto.getLocation() != null) {
            event.setLocation(eventDto.getLocation());
        }
        if (eventDto.getTags() != null) {
            event.getTags()
                    .addAll(eventDto.getTags().stream().map(TagMapper.INSTANCE::mapToEntity).collect(Collectors.toList()));
        }
    }
}
