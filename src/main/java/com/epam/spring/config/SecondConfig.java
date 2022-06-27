package com.epam.spring.config;

import com.epam.spring.beans.BeanB;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.epam.spring.beans")
public class FirstConfig {
    @Bean
    public BeanB beanB(){
        return new BeanB();
    }
}
