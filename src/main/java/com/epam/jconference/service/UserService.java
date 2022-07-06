package com.epam.jconference.service;

import com.epam.jconference.dto.UserDto;
import com.epam.jconference.model.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    UserDto create(UserDto user);

    UserDto login(UserDto user);

    UserDto update(UserDto userDto);

    ResponseEntity<Void> logout();

    UserDto profile();

    UserDto getById(Long id);
}
