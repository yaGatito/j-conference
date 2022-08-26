package com.epam.jconference.controller;

import com.epam.jconference.api.UserApi;
import com.epam.jconference.controller.assembler.UserAssembler;
import com.epam.jconference.controller.model.UserModel;
import com.epam.jconference.dto.UserDto;
import com.epam.jconference.model.enums.UserRole;
import com.epam.jconference.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    private final UserAssembler userAssembler;

    public UserModel create(UserDto userDto) {
        UserDto createdUserDto = userService.create(userDto);
        return userAssembler.toModel(createdUserDto);
    }

    public UserModel update(UserDto userDto) {
        UserDto updatedUser = userService.update(userDto);
        return userAssembler.toModel(updatedUser);
    }

    public UserModel getByEmail(String email) {
        UserDto userDto = userService.getByEmail(email);
        return userAssembler.toModel(userDto);
    }

    @Override
    public List<UserDto> getAllByRole(String role) {
        return userService.getAllByRole(UserRole.valueOf(role.toUpperCase()));
    }

    @Async
    public UserModel login(UserDto userDto) {
        UserDto loggedUser = userService.login(userDto);
        return userAssembler.toModel(loggedUser);
    }

    public ResponseEntity<Void> logout() {
        return userService.logout();
    }

    public UserModel profile() {
        UserDto userProfile = userService.profile();
        return userAssembler.toModel(userProfile);
    }

    @Override
    public UserModel setUserRole(String role, String email) {
        UserDto userDto = userService.setUserRole(UserRole.valueOf(role.toUpperCase()), email);
        return userAssembler.toModel(userDto);
    }
}
