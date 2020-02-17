package com.dkha.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author Spring
 * @Since 2019/9/21 9:48
 * @Description 用于在Spring框架无法注入的情况下获取对应的Bean
 */
@Component
public class SpringBeanFactoryUtils implements ApplicationContextAware {

    private static ApplicationContext context = null;

    public static <T> T getBean(Class<T> type) {
        return context.getBean(type);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringBeanFactoryUtils.context == null) {
            SpringBeanFactoryUtils.context = applicationContext;
        }
    }
}
