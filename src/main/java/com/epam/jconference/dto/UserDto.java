package com.epam.jconference.dto;

import com.epam.jconference.dto.group.OnCreate;
import com.epam.jconference.dto.group.OnLogin;
import com.epam.jconference.dto.group.OnUpdate;
import com.epam.jconference.dto.validation.strings.StringItem;
import com.epam.jconference.dto.validation.strings.ValidateString;
import com.epam.jconference.model.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    @Null(message = "{user.id.null}", groups = {OnCreate.class, OnLogin.class, OnUpdate.class})
    private Long id;

    @Null(message = "{user.role.null}", groups = {OnCreate.class, OnLogin.class, OnUpdate.class})
    private UserRole role;

    @NotBlank(message = "{user.name.not_blank}", groups = {OnCreate.class})
    @ValidateString(value = StringItem.NAME, groups = {OnCreate.class, OnUpdate.class})
    @Null(message = "{user.name.null}{user.lastname.null}", groups = {OnLogin.class})
    private String name;

    @NotBlank(message = "{user.lastname.not_blank}", groups = {OnCreate.class})
    @ValidateString(value = StringItem.LASTNAME, groups = {OnCreate.class, OnUpdate.class})
    @Null(message = "{user.lastname.null}", groups = {OnLogin.class})
    private String lastname;

    @Email(message = "{user.email}", groups = {OnCreate.class, OnLogin.class})
    @NotBlank(message = "{user.lastname.not_blank}", groups = {OnCreate.class, OnUpdate.class, OnLogin.class})
    private String email;

    @NotBlank(message = "{user.password.not_blank}", groups = {OnCreate.class, OnLogin.class})
    @ValidateString(value = StringItem.PASSWORD, groups = {OnCreate.class})
    @Null(message = "{user.password.null}", groups = OnUpdate.class)
    private String password;

    @Null(message = "{user.notifications.null}", groups = {OnCreate.class, OnLogin.class})
    private Boolean notifications;
}
