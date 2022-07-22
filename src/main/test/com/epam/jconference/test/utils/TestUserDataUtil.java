package com.epam.jconference.test.utils;

import com.epam.jconference.dto.UserDto;
import com.epam.jconference.model.User;
import com.epam.jconference.model.enums.UserRole;

public class TestUserDataUtil {
    public static final Long USER_ID = 1L;
    public static final String USER_NAME = "Name";
    public static final UserRole USER_ROLE = UserRole.USER;
    public static final String USER_LASTNAME = "Lastname";
    public static final String USER_EMAIL = "qwerty@email.com";
    public static final String USER_PWD = "q2345678";
    public static final Boolean USER_NOTIFICATIONS = true;

    public static User createUser() {
        return User.builder()
                   .id(USER_ID)
                   .name(USER_NAME)
                   .lastname(USER_LASTNAME)
                   .email(USER_EMAIL)
                   .password(USER_PWD)
                   .notifications(true)
                   .role(UserRole.USER)
                   .build();
    }

    public static UserDto createUserDto() {
        return UserDto.builder()
                      .name(USER_NAME)
                      .lastname(USER_LASTNAME)
                      .email(USER_EMAIL)
                      .password(USER_PWD)
                      .build();
    }

}
