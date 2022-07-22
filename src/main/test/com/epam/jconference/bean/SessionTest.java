package com.epam.jconference.bean;

import com.epam.jconference.exception.InvalidOperationException;
import com.epam.jconference.exception.UnauthorizedAccessException;
import com.epam.jconference.model.User;
import com.epam.jconference.model.enums.UserRole;
import com.epam.jconference.test.utils.TestUserDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SessionTest {

    private final Session session = new Session();

    @Test
    void loginTest() {
        User user = TestUserDataUtil.createUser();
        session.login(user);
        User sessionUser = session.getUser();
        assertEquals(user, sessionUser);
    }

    @Test
    void loginAlreadyLoggedUserTest() {
        User user = TestUserDataUtil.createUser();
        session.login(user);
        assertThrows(InvalidOperationException.class, () -> session.login(user));
    }

    @Test
    void logoutTest() {
        User user = TestUserDataUtil.createUser();
        session.login(user);
        session.logout();
        assertNull(session.getUser());
    }

    @Test
    void invalidLogoutTest() {
        User user = TestUserDataUtil.createUser();
        assertThrows(InvalidOperationException.class, session::logout);
    }

    @Test
    void profileTest() {
        User user = TestUserDataUtil.createUser();
        session.login(user);
        User profile = session.profile();
        assertEquals(user, profile);
    }

    @Test
    void unauthorizedProfileTest() {
        assertThrows(UnauthorizedAccessException.class, session::profile);
    }

    @Test
    void isLoggedTest() {
        assertThrows(UnauthorizedAccessException.class, session::isLogged);
    }

    @Test
    void isModerTest() {
        User user = TestUserDataUtil.createUser();
        user.setRole(UserRole.SPEAKER);
        session.login(user);
        assertThrows(UnauthorizedAccessException.class, session::isModer);
    }

    @Test
    void isModerTest2() {
        User user = TestUserDataUtil.createUser();
        user.setRole(UserRole.USER);
        session.login(user);
        assertThrows(UnauthorizedAccessException.class, session::isModer);
    }

    @Test
    void isSpeakerTest() {
        User user = TestUserDataUtil.createUser();
        user.setRole(UserRole.USER);
        session.login(user);
        assertThrows(UnauthorizedAccessException.class, session::isSpeaker);
    }

    @Test
    void isSpeakerTest2() {
        User user = TestUserDataUtil.createUser();
        user.setRole(UserRole.MODER);
        session.login(user);
        assertThrows(UnauthorizedAccessException.class, session::isSpeaker);
    }
}
