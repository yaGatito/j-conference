package com.epam.jconference.bean;

import com.epam.jconference.model.User;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Data
@Component
@SessionScope
public class Session {

    private User user;
}
