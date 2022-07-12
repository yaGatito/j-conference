package com.epam.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.epam.spring.bfpp", "com.epam.spring.bpp"})
public class SecondConfig {
}
