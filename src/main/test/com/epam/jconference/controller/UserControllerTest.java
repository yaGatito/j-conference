package com.epam.jconference.controller;

import com.epam.jconference.config.TestConfig;
import com.epam.jconference.controller.assembler.UserAssembler;
import com.epam.jconference.controller.model.UserModel;
import com.epam.jconference.dto.UserDto;
import com.epam.jconference.mapper.UserMapper;
import com.epam.jconference.model.enums.ErrorType;
import com.epam.jconference.model.enums.UserRole;
import com.epam.jconference.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.epam.jconference.test.utils.TestUserDataUtil.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@Import(TestConfig.class)
public class UserControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = "/api/v1/users";
    @MockBean
    private UserService userService;
    @MockBean
    private UserAssembler userAssembler;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getUserByEmailTest() throws Exception {
        UserDto userDto = UserMapper.INSTANCE.mapUserDto(createUser());
        UserModel userModel = new UserModel(userDto);

        when(userService.getByEmail(USER_EMAIL)).thenReturn(userDto);
        when(userAssembler.toModel(userDto)).thenReturn(userModel);

        mockMvc.perform(get(baseUrl + "/" + USER_EMAIL))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id").value(USER_ID))
               .andExpect(jsonPath("$.name").value(userDto.getName()))
               .andExpect(jsonPath("$.lastname").value(userDto.getLastname()))
               .andExpect(jsonPath("$.email").value(userDto.getEmail()))
               .andExpect(jsonPath("$.role").value(userDto.getRole().toString()));
    }

    @Test
    void createUserTest() throws Exception {
        UserDto userDto = createUserDto();
        UserDto persistedUserDto = UserMapper.INSTANCE.mapUserDto(createUser());
        UserModel userModel = new UserModel(persistedUserDto);

        when(userService.create(userDto)).thenReturn(persistedUserDto);
        when(userAssembler.toModel(persistedUserDto)).thenReturn(userModel);

        String body = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post(baseUrl).content(body).contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id").value(USER_ID))
               .andExpect(jsonPath("$.name").value(userDto.getName()))
               .andExpect(jsonPath("$.lastname").value(userDto.getLastname()))
               .andExpect(jsonPath("$.email").value(userDto.getEmail()))
               .andExpect(jsonPath("$.role").value(UserRole.USER.toString()));
    }

    @Test
    void createInvalidUserTest() throws Exception {
        UserDto userDto = createUserDto();
        userDto.setEmail(null);

        String body = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post(baseUrl).content(body).contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name())
               );

        userDto = createUserDto();
        userDto.setPassword(null);
        body = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post(baseUrl).content(body).contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name())
               );

        userDto = createUserDto();
        userDto.setName(null);
        body = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post(baseUrl).content(body).contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name())
               );

        userDto = createUserDto();
        userDto.setLastname(null);
        body = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post(baseUrl).content(body).contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name())
               );

        userDto = createUserDto();
        userDto.setPassword("");
        body = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post(baseUrl).content(body).contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name())
               );

        userDto = createUserDto();
        userDto.setEmail("");
        body = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post(baseUrl).content(body).contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name())
               );

        userDto = createUserDto();
        userDto.setName("");
        body = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post(baseUrl).content(body).contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name())
               );

        userDto = createUserDto();
        userDto.setLastname("");
        body = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post(baseUrl).content(body).contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name())
               );
    }

    @Test
    void updateTest() throws Exception {
        String newLastName = "NEWLASTNAME";
        UserDto userDto = UserDto
                .builder()
                .email(USER_EMAIL)
                .lastname(newLastName)
                .build();
        UserDto persistedUserDto = UserMapper.INSTANCE.mapUserDto(createUser());
        persistedUserDto.setLastname(userDto.getLastname());
        UserModel userModel = new UserModel(persistedUserDto);

        when(userService.update(userDto)).thenReturn(persistedUserDto);
        when(userAssembler.toModel(persistedUserDto)).thenReturn(userModel);

        String body = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(patch(baseUrl).content(body).contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpectAll(status().isOk(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$.id").value(USER_ID),
                       jsonPath("$.name").value(persistedUserDto.getName()),
                       jsonPath("$.lastname").value(newLastName),
                       jsonPath("$.email").value(persistedUserDto.getEmail()),
                       jsonPath("$.role").value(persistedUserDto.getRole().toString())
               );
    }

    @Test
    void updateInvalidUserTest() throws Exception {
        String errorType = ErrorType.VALIDATION_ERROR_TYPE.name();
        UserDto userDto = UserDto
                .builder()
                .email(null)
                .lastname("Qwerty")
                .build();

        String body = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(patch(baseUrl).content(body).contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(errorType)
               );
    }

    @Test
    void getAllByRoleTest() throws Exception {
        UserDto userDto = UserMapper.INSTANCE.mapUserDto(createUser());
        UserRole role = UserRole.SPEAKER;
        userDto.setRole(role);
        when(userService.getAllByRole(role)).thenReturn(List.of(userDto));

        mockMvc.perform(get(baseUrl + "/role/" + role.toString().toLowerCase()))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].id").value(USER_ID))
               .andExpect(jsonPath("$[0].name").value(userDto.getName()))
               .andExpect(jsonPath("$[0].lastname").value(userDto.getLastname()))
               .andExpect(jsonPath("$[0].email").value(userDto.getEmail()))
               .andExpect(jsonPath("$[0].role").value(role.toString()));
    }

    @Test
    void loginTest() throws Exception {
        UserDto userDto = UserDto.builder()
                                 .password(USER_PWD)
                                 .email(USER_EMAIL)
                                 .build();
        UserDto persistedUserDto = UserMapper.INSTANCE.mapUserDto(createUser());
        UserModel userModel = new UserModel(persistedUserDto);

        when(userService.login(userDto)).thenReturn(persistedUserDto);
        when(userAssembler.toModel(persistedUserDto)).thenReturn(userModel);

        String body = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post(baseUrl + "/login").content(body).contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id").value(USER_ID))
               .andExpect(jsonPath("$.name").value(persistedUserDto.getName()))
               .andExpect(jsonPath("$.lastname").value(persistedUserDto.getLastname()))
               .andExpect(jsonPath("$.email").value(USER_EMAIL))
               .andExpect(jsonPath("$.role").value(persistedUserDto.getRole().toString()));
    }

    @Test
    void logoutTest() throws Exception {
        when(userService.logout()).thenReturn(ResponseEntity.ok().build());
        mockMvc.perform(post(baseUrl + "/logout"))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    void profileTest() throws Exception {
        UserDto persistedUserDto = UserMapper.INSTANCE.mapUserDto(createUser());
        UserModel userModel = new UserModel(persistedUserDto);

        when(userService.profile()).thenReturn(persistedUserDto);
        when(userAssembler.toModel(persistedUserDto)).thenReturn(userModel);

        mockMvc.perform(get(baseUrl + "/profile"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id").value(persistedUserDto.getId()))
               .andExpect(jsonPath("$.name").value(persistedUserDto.getName()))
               .andExpect(jsonPath("$.lastname").value(persistedUserDto.getLastname()))
               .andExpect(jsonPath("$.email").value(persistedUserDto.getEmail()))
               .andExpect(jsonPath("$.role").value(persistedUserDto.getRole().toString()));
    }

    @Test
    void setUserRoleTest() throws Exception {
        UserRole role = UserRole.MODER;
        String email = USER_EMAIL;
        UserDto persistedUser = UserMapper.INSTANCE.mapUserDto(createUser());
        persistedUser.setRole(role);
        persistedUser.setEmail(email);
        UserModel userModel = new UserModel(persistedUser);
        when(userService.setUserRole(role, email)).thenReturn(persistedUser);
        when(userAssembler.toModel(persistedUser)).thenReturn(userModel);

        mockMvc.perform(patch(baseUrl + "/role?role=" + role.toString().toLowerCase() + "&email=" + email))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.email").value(email))
               .andExpect(jsonPath("$.role").value(role.toString()));
    }


    @Test
    void setInvalidUserRoleTest() throws Exception {
        String role = "notExistedRole";
        String email = USER_EMAIL;

        mockMvc.perform(patch(baseUrl + "/role?role=" + role + "&email=" + email))
               .andDo(print())
               .andExpect(status().is4xxClientError())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.errorType").value(ErrorType.INVALID_DATA_ERROR_TYPE.name()));
    }
}
