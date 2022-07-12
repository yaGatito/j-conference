package com.epam.jconference.service.mapper;

import com.epam.jconference.controller.dto.LectureDto;
import com.epam.jconference.service.model.Lecture;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LectureMapper {
    LectureMapper INSTANCE = Mappers.getMapper(LectureMapper.class);

    Lecture mapToEntity(LectureDto lectureDto);

    LectureDto mapToDto(Lecture lecture);
}
