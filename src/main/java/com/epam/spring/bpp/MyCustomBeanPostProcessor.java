package com.epam.spring.bpp;

import com.epam.spring.beans.BeanAbstract;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class MyCustomBeanPostProcessor implements BeanPostProcessor {
    private static void validate(Object beanObject) {
        if (beanObject instanceof BeanAbstract) {
            BeanAbstract bean = (BeanAbstract) beanObject;
            String message;
            if (bean.getName() != null && bean.getValue() > 0) {
                message = beanObject.getClass().getSimpleName() + " is valid";
            } else {
                message = beanObject.getClass().getSimpleName() + " is invalid";
            }
            System.out.println(message);
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        validate(bean);
        return bean;
    }
}
