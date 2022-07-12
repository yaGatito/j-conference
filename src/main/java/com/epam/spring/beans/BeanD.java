package com.epam.spring.beans;

public class BeanD extends BeanAbstract {

    public BeanD(String name, Integer value) {
        super(name, value);
    }

    public void initMethod() {
        System.out.println(this.getClass().getSimpleName() + " init");
    }

    public void destroyMethod() {
        System.out.println(this.getClass().getSimpleName() + " destroy");
    }
}
