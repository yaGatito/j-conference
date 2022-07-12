package com.epam.spring.beans;

public abstract class BeanAbstract {
    private String name;
    private Integer value;

    public BeanAbstract(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public BeanAbstract() {
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + "name='" + name + '\'' + ", value='" + value + '\'' + '}';
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }
}
