package com.epam.jconference.controller;

import com.epam.jconference.controller.dto.UserDto;
import com.epam.jconference.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users/registration")
    public UserDto registration(@RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PostMapping("/users/login")
    public Boolean login(@RequestBody UserDto userDto) {
        return null;
    }

    @PostMapping("/users/logout")
    public Boolean logout() {
        return null;
    }

    @GetMapping("/users/profile")
    public UserDto profile() {
        return null;
    }

}
