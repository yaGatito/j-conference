package com.epam.spring.bfpp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MyCustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        Arrays.stream(configurableListableBeanFactory.getBeanDefinitionNames())
                .filter(beanName->beanName.equals("beanB"))
                .forEach(beanName->{
                    BeanDefinition beanDefinition = configurableListableBeanFactory.getBeanDefinition(beanName);
                    beanDefinition.setInitMethodName("secondInitMethod");
                });
    }
}
