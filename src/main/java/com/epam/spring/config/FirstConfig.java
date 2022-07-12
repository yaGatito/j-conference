package com.epam.spring.config;

import com.epam.spring.beans.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan("com.epam.spring.beans")
@Import(SecondConfig.class)
public class FirstConfig {

    @Bean
    public BeanA beanA() {
        return new BeanA();
    }

    @Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
    @DependsOn("beanD")
    public BeanB beanB(@Value("${beanB.name}") String name, @Value("${beanB.value}") Integer value) {
        return new BeanB(name, value);
    }

    @Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
    @DependsOn("beanB")
    public BeanC beanC(@Value("${beanC.name}") String name, @Value("${beanC.value}") Integer value) {
        return new BeanC(name, value);
    }

    @Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
    public BeanD beanD(@Value("${beanD.name}") String name, @Value("${beanD.value}") Integer value) {
        return new BeanD(name, value);
    }

    @Bean
    public BeanE beanE() {
        return new BeanE();
    }

    @Bean
    @Lazy
    public BeanF beanF() {
        return new BeanF();
    }
}
