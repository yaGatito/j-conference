package com.epam.spring.beans;


public class BeanB extends BeanAbstract {

    public BeanB(String name, Integer value) {
        super(name, value);
    }

    public void initMethod() {
        System.out.println(this.getClass().getSimpleName() + " init");
    }

    public void secondInitMethod() {
        System.out.println(this.getClass().getSimpleName() + " init (changed by BFPP) ");
    }

    public void destroyMethod() {
        System.out.println(this.getClass().getSimpleName() + " destroy");
    }
}
