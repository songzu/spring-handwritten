package com.songzuedu.spring.framework.beans.config;

/**
 * <p></p>
 *
 * @author gengen.wang
 **/
public class SZBeanPostProcessor {

    //为在 Bean 的初始化前提供回调入口
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    //为在 Bean 的初始化之后提供回调入口
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

}
