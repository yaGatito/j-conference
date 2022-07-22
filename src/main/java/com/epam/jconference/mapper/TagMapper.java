package com.epam.jconference.mapper;

import com.epam.jconference.model.Tag;
import com.epam.jconference.dto.TagDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    Tag mapToEntity(TagDto tagDto);

    TagDto mapToDto(Tag tag);
}
