package com.epam.jconference.model;

import com.epam.jconference.model.enums.UserRole;
import lombok.Data;

@Data
public class User {

    private Long id;
    private UserRole role;
    private String name;
    private String lastname;
    private String email;
    private String password;
    private Boolean notifications = true;
}
