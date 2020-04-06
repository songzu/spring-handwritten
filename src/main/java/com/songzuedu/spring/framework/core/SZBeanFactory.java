package com.songzuedu.spring.framework.core;

/**
 * <p>单例工厂的顶层设计</p>
 *
 * @author gengen.wang
 **/
public interface SZBeanFactory {

    /**
     * 根据beanName从IOC容器中获得一个实例bean
     *
     * @param beanName
     * @return
     */
    Object getBean(String beanName) throws Exception;


    Object getBean(Class<?> beanClass) throws Exception;
}
