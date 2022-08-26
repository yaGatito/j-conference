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

    @Null(message = "{id}{null}", groups = {OnCreate.class, OnLogin.class, OnUpdate.class})
    private Long id;

    @Null(message = "{role}{null}", groups = {OnCreate.class, OnLogin.class, OnUpdate.class})
    private UserRole role;

    @NotBlank(message = "{name}{not_blank}", groups = {OnCreate.class})
    @ValidateString(message = "{name}{invalid}", value = StringItem.NAME, groups = {OnCreate.class, OnUpdate.class})
    @Null(message = "{name}{null}", groups = {OnLogin.class})
    private String name;

    @NotBlank(message = "{lastname}{not_blank}", groups = {OnCreate.class})
    @ValidateString(message = "{lastname}{invalid}", value = StringItem.LASTNAME, groups = {OnCreate.class, OnUpdate.class})
    @Null(message = "{lastname}{null}", groups = {OnLogin.class})
    private String lastname;

    @Email(message = "{email}{invalid}", groups = {OnCreate.class, OnLogin.class})
    @NotBlank(message = "{lastname}{not_blank}", groups = {OnCreate.class, OnUpdate.class, OnLogin.class})
    private String email;

    @NotBlank(message = "{password}{not_blank}", groups = {OnCreate.class, OnLogin.class})
    @ValidateString(message = "{password}{invalid}", value = StringItem.PASSWORD, groups = {OnCreate.class})
    @Null(message = "{password}{null}", groups = OnUpdate.class)
    private String password;

    @Null(message = "{notifications}{null}", groups = {OnCreate.class, OnLogin.class})
    private Boolean notifications;
}
