package com.epam.jconference.service;

import com.epam.jconference.dto.UserDto;
import com.epam.jconference.model.enums.UserRole;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    UserDto create(UserDto user);

    UserDto login(UserDto user);

    UserDto update(UserDto userDto);

    ResponseEntity<Void> logout();

    UserDto profile();

    UserDto getByEmail(String email);

    UserDto setUserRole(UserRole role, String email);

    List<UserDto> getAllByRole(UserRole role);
}
