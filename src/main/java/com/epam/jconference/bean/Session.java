package com.epam.jconference.bean;

import com.epam.jconference.exception.InvalidOperationException;
import com.epam.jconference.exception.UnauthorizedAccessException;
import com.epam.jconference.model.User;
import com.epam.jconference.model.enums.UserRole;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Component
@SessionScope
public class Session {

    private final Map<String, LocalTime> sessionLog;
    private User user;

    public Session() {
        sessionLog = new HashMap<>();
    }

    public void logout() {
        if (Objects.nonNull(user)) {
            sessionLog.put("logout " + user.getId(), LocalTime.now());
            user = null;
        } else {
            throw new InvalidOperationException("Invalid logout");
        }
    }

    public void login(User user) {
        if (Objects.isNull(this.user)) {
            this.user = user;
            sessionLog.put("login " + user.getId(), LocalTime.now());
        } else {
            throw new InvalidOperationException("Invalid login. The already logged in user should logout");
        }
    }

    public User profile() {
        isLogged();
        return user;
    }

    public void isLogged() {
        if (Objects.isNull(user)) {
            throw new UnauthorizedAccessException("User should be authorized");
        }
    }

    public void isModer() {
        isLogged();
        if (!user.getRole().equals(UserRole.MODER)) {
            throw new UnauthorizedAccessException("User role should be moder role");
        }
    }

    public void isSpeaker() {
        isLogged();
        if (!user.getRole().equals(UserRole.MODER)) {
            throw new UnauthorizedAccessException("User role should be moder role");
        }
    }
}
