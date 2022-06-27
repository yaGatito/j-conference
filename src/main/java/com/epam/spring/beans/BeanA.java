package com.epam.spring.beans;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class BeanA extends BeanGeneral implements InitializingBean, DisposableBean {

    @Override
    public void destroy() throws Exception {
        System.out.println(this.getClass().getSimpleName() + " destroy");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(this.getClass().getSimpleName() + " init");
    }
}
