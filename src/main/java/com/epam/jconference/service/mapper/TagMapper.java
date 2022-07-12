package com.epam.jconference.service.mapper;

import com.epam.jconference.controller.dto.TagDto;
import com.epam.jconference.service.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagMapper {
    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    Tag mapToEntity(TagDto tagDto);

    TagDto mapToDto(Tag tag);
}
