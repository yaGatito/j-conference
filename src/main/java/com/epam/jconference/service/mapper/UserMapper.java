package com.epam.jconference.service.mapper;

import com.epam.jconference.controller.dto.UserDto;
import com.epam.jconference.service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User mapUser(UserDto userDto);

    UserDto mapUserDto(User user);
}
