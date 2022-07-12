package com.epam.spring.beans;

public abstract class BeanGeneral {
    private String name;
    private Integer value;

    public BeanGeneral(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public BeanGeneral() {
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
