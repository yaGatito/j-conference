package com.epam.jconference.mapper;

import com.epam.jconference.model.Lecture;
import com.epam.jconference.dto.LectureDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LectureMapper {

    LectureMapper INSTANCE = Mappers.getMapper(LectureMapper.class);

    Lecture mapToEntity(LectureDto lectureDto);

    LectureDto mapToDto(Lecture lecture);
}
