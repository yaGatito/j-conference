package com.epam.jconference.api;

import com.epam.jconference.controller.model.UserModel;
import com.epam.jconference.dto.UserDto;
import com.epam.jconference.dto.group.OnCreate;
import com.epam.jconference.dto.group.OnLogin;
import com.epam.jconference.dto.group.OnUpdate;
import com.epam.jconference.dto.validation.enums.EnumConstraint;
import com.epam.jconference.model.enums.UserRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import java.util.List;

@Api(tags = "User management API")
@RequestMapping("/api/v1/users")
@Validated
public interface UserApi {

    @ApiOperation("Create user")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    UserModel create(@RequestBody @Validated(OnCreate.class) UserDto userDto);

    @ApiOperation("Update user")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping
    UserModel update(@RequestBody @Validated(OnUpdate.class) UserDto userDto);

    @ApiOperation("Get user")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", paramType = "path", required = true,
            value = "User id")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{email}")
    UserModel getByEmail(@PathVariable @Email(message = "{email}{invalid}") String email);

    @ApiOperation("Get all users by role")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/role/{role}")
    List<UserDto> getAllByRole(
            @PathVariable @EnumConstraint(
                    message = "{role}{not_exist}",
                    value = UserRole.class)
            String role
    );

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

    @ApiOperation("Sets specified role to specified user")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "role",
                    type = "requestParam",
                    required = true,
                    value = "Role that will be " + "established"),
            @ApiImplicitParam(
                    name = "user_id",
                    type = "requestParam",
                    required = true,
                    value = "ID of user of which role will be established")
    })
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/role")
    UserModel setUserRole(
            @RequestParam("role")
            @EnumConstraint(
                    message = "{role}{not_exist}",
                    value = UserRole.class)
            String role,
            @RequestParam("email")
            @Email(message = "{email}{invalid}")
            String email
    );
}
