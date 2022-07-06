package com.epam.jconference.api;

import com.epam.jconference.controller.model.UserModel;
import com.epam.jconference.dto.UserDto;
import com.epam.jconference.dto.group.OnCreate;
import com.epam.jconference.dto.group.OnLogin;
import com.epam.jconference.dto.group.OnUpdate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "User management API")
@RequestMapping("/api/v1/users")
public interface UserApi {

    @ApiOperation("Create user")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    UserModel create(@RequestBody @Validated(OnCreate.class) UserDto userDto);

    @ApiOperation("Update user")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    UserModel update(@RequestBody @Validated(OnUpdate.class) UserDto userDto);

    @ApiOperation("Get user")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", paramType = "path", required = true, value = "User id")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    UserModel getById(@PathVariable Long id);

    @ApiOperation("Login user")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    UserModel login(@RequestBody @Validated(OnLogin.class) UserDto userDto);

    @ApiOperation("Logout user")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/logout")
    ResponseEntity<Void> logout();

    @ApiOperation("User profile")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/profile")
    UserModel profile();
}
