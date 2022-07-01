package com.epam.jconference.service;

import com.epam.jconference.service.model.User;
import com.epam.jconference.controller.dto.UserDto;
import com.epam.jconference.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public UserDto create(UserDto userDto){
        User user = UserMapper.INSTANCE.mapUser(userDto);
        return userDto;
    }
}
