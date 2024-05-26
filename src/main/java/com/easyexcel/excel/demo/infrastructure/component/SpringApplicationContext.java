package com.easyexcel.excel.demo.infrastructure.component;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SpringApplicationContext implements ApplicationContextAware {
    private static ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringApplicationContext.applicationContext = applicationContext;
    }

    public static <T> T getStaticBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getStaticBean(Class<T> clazz, String name) {
        return applicationContext.getBean(name, clazz);
    }

    public static <T> List<T> getStaticBeans(Class<T> clazz) {
        return applicationContext.getBeanProvider(clazz).stream().collect(Collectors.toList());
    }

    public <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public <T> T getBean(Class<T> clazz, String name) {
        return applicationContext.getBean(name, clazz);
    }
}
