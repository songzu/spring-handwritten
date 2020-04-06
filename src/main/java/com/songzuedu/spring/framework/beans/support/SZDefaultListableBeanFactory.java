package com.songzuedu.spring.framework.beans.support;


import com.songzuedu.spring.framework.beans.config.SZBeanDefinition;
import com.songzuedu.spring.framework.context.support.SZAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p></p>
 *
 * @author gengen.wang
 **/
public class SZDefaultListableBeanFactory extends SZAbstractApplicationContext {

    //存储注册信息的 BeanDefinition
    protected final Map<String, SZBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

}
