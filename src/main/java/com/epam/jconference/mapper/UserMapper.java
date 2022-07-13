package com.epam.jconference.mapper;

import com.epam.jconference.dto.UserDto;
import com.epam.jconference.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Objects;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User mapUser(UserDto userDto);

    UserDto mapUserDto(User user);

    default User populateUserWithPresentUserDtoFields(User persisted, UserDto userDto){
        if (Objects.nonNull(userDto.getNotifications())){
            persisted.setNotifications(userDto.getNotifications());
        }
        if (Objects.nonNull(userDto.getLastname())){
            persisted.setLastname(userDto.getLastname());
        }
        if (Objects.nonNull(userDto.getName())){
            persisted.setName(userDto.getName());
        }
        return persisted;
    }

}
