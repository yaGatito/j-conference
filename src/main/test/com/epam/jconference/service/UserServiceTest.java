package com.epam.jconference.service;

import com.epam.jconference.bean.Session;
import com.epam.jconference.dto.UserDto;
import com.epam.jconference.exception.EntityNotFoundException;
import com.epam.jconference.exception.InvalidOperationException;
import com.epam.jconference.exception.UnauthorizedAccessException;
import com.epam.jconference.model.User;
import com.epam.jconference.model.enums.UserRole;
import com.epam.jconference.repository.UserRepository;
import com.epam.jconference.service.impl.UserServiceImpl;
import com.epam.jconference.test.utils.TestUserDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.epam.jconference.test.utils.TestUserDataUtil.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Spy
    private final Session session = new Session();
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUserTest() {
        User user = TestUserDataUtil.createUser();
        when(userRepository.getByEmail(USER_EMAIL)).thenReturn(Optional.ofNullable(user));

        UserDto userDto = userService.getByEmail(USER_EMAIL);

        assertThat(userDto, allOf(
                hasProperty("id", equalTo(user.getId())),
                hasProperty("name", equalTo(user.getName())),
                hasProperty("lastname", equalTo(user.getLastname())),
                hasProperty("email", equalTo(user.getEmail())),
                hasProperty("role", equalTo(user.getRole())),
                hasProperty("notifications", equalTo(user.getNotifications()))
        ));
    }

    @Test
    void createUserTest() {
        UserDto testUserDto = TestUserDataUtil.createUserDto();
        User testUser = TestUserDataUtil.createUser();
        when(userRepository.save(any())).thenReturn(testUser);

        UserDto responseUserDto = userService.create(testUserDto);

        assertThat(responseUserDto, allOf(
                hasProperty("id", equalTo(USER_ID)),
                hasProperty("name", equalTo(testUserDto.getName())),
                hasProperty("lastname", equalTo(testUserDto.getLastname())),
                hasProperty("email", equalTo(testUserDto.getEmail())),
                hasProperty("role", equalTo(USER_ROLE)),
                hasProperty("notifications", equalTo(USER_NOTIFICATIONS))
        ));
    }

    @Test
    void createAlreadyExistedUser() {
        UserDto userDto = TestUserDataUtil.createUserDto();
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);
        assertThrows(InvalidOperationException.class, () -> userService.create(userDto));
    }

    @Test
    void getUserEntityNotFoundException() {
        when(userRepository.getByEmail(USER_EMAIL)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.getByEmail(USER_EMAIL));
    }

    @Test
    void loginUserTest() {
        UserDto testUserDto = TestUserDataUtil.createUserDto();
        User testUser = TestUserDataUtil.createUser();
        when(userRepository.getByEmail(testUserDto.getEmail())).thenReturn(Optional.of(testUser));

        userService.login(testUserDto);
        User sessionUser = session.getUser();

        assertEquals(sessionUser, testUser);
    }

    @Test
    void userPasswordMismatchInLoginUserTest() {
        UserDto userDto = createUserDto();
        userDto.setPassword("12345");
        User user = createUser();
        when(userRepository.getByEmail(userDto.getEmail())).thenReturn(Optional.of(user));

        assertThrows(UnauthorizedAccessException.class, () -> userService.login(userDto));
    }

    @Test
    void userDoesntExistByEmailInLoginUserTest() {
        UserDto userDto = createUserDto();
        when(userRepository.getByEmail(userDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.login(userDto));
    }

    @Test
    void profileTestNull() {
        assertThrows(UnauthorizedAccessException.class, () -> userService.profile());
    }

    @Test
    void profileTest() {
        User user = createUser();
        session.login(user);

        UserDto profile = userService.profile();

        assertThat(profile, allOf(
                hasProperty("name", equalTo(user.getName())),
                hasProperty("lastname", equalTo(user.getLastname())),
                hasProperty("email", equalTo(user.getEmail())),
                hasProperty("role", equalTo(user.getRole())),
                hasProperty("notifications", equalTo(user.getNotifications()))
        ));
    }

    @Test
    void logoutTest() {
        assertThrows(UnauthorizedAccessException.class, () -> userService.profile());

        User user = createUser();
        session.login(user);

        UserDto profile = userService.profile();
        assertThat(profile, allOf(
                hasProperty("name", equalTo(user.getName())),
                hasProperty("lastname", equalTo(user.getLastname())),
                hasProperty("email", equalTo(user.getEmail())),
                hasProperty("role", equalTo(user.getRole())),
                hasProperty("notifications", equalTo(user.getNotifications()))
        ));

        userService.logout();
        assertThrows(UnauthorizedAccessException.class, () -> userService.profile());
    }

    @Test
    void invalidLogout() {
        assertThrows(InvalidOperationException.class, () -> userService.logout());
    }

    @Test
    void updateUser() {
        String newLastName = "LastnameDiff";
        boolean newNotifications = false;

        UserDto userDto = createUserDto();
        userDto.setLastname(newLastName);
        userDto.setNotifications(newNotifications);
        User user = createUser();
        when(userRepository.getByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserDto persistedUserDto = userService.update(userDto);

        assertThat(persistedUserDto, allOf(
                hasProperty("name", equalTo(userDto.getName())),
                hasProperty("lastname", equalTo(userDto.getLastname())),
                hasProperty("notifications", equalTo(userDto.getNotifications()))
        ));
    }

    @Test
    void setUserRoleTest() {
        User user = createUser();
        when(userRepository.getByEmail(any())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        UserRole role = UserRole.SPEAKER;
        UserDto userDto = userService.setUserRole(role, USER_EMAIL);

        assertEquals(userDto.getRole(), role);
        assertEquals(userDto.getEmail(), USER_EMAIL);
    }

    @Test
    void setUserRoleToNotExistedUserTest() {
        when(userRepository.getByEmail(any())).thenReturn(Optional.empty());
        UserRole role = UserRole.SPEAKER;

        assertThrows(EntityNotFoundException.class, () -> userService.setUserRole(role, USER_EMAIL));
    }

    @Test
    void getAllByRole() {
        UserRole role = UserRole.USER;
        User user = createUser();
        when(userRepository.findAllByRole(role)).thenReturn(List.of(user));
        List<UserDto> allByRole = userService.getAllByRole(role);
        assertEquals(allByRole.size(), allByRole.stream().filter(u -> u.getRole().equals(role)).count());
    }

}
