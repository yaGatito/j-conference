package com.epam.spring.bfpp;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class MyCustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        String[] beanDefinitionNames = configurableListableBeanFactory.getBeanDefinitionNames();
        for (String definitionName : beanDefinitionNames) {
            if (definitionName.equals("beanB")){
                BeanDefinition beanB = configurableListableBeanFactory.getBeanDefinition(definitionName);
                beanB.setInitMethodName("secondInitMethod");
            }
        }
    }
}
