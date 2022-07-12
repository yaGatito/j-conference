package com.epam.spring.beans;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class BeanE extends BeanGeneral {

    @PostConstruct
    public void init() {
        System.out.println(this.getClass().getSimpleName() + " init");
    }


    @PreDestroy
    public void destroy() {
        System.out.println(this.getClass().getSimpleName() + " destroy");
    }
}
