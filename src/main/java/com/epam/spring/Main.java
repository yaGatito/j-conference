package com.epam.spring;

import com.epam.spring.config.FirstConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx
                = new AnnotationConfigApplicationContext(FirstConfig.class);

        Arrays.stream(ctx.getBeanDefinitionNames()).forEach(x->System.out.println(ctx.getBean(x)));

        ctx.close();
    }
}
