package com.epam.jconference.controller.dto;

import com.epam.jconference.service.model.enums.UserRole;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserDto {
    private Long id;
    private UserRole role;
    @NonNull
    private String name;
    @NonNull
    private String lastname;
    @NonNull
    private String email;
    @NonNull
    private String password;
    private Boolean notifications = true;
}
