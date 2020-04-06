package com.songzuedu.spring.framework.aop.aspect;

import java.lang.reflect.Method;

/**
 * <p></p>
 *
 * @author gengen.wang
 **/
public interface SZJoinPoint {

    Object getThis();

    Object[] getArguments();

    Method getMethod();

    void setUserAttribute(String key, Object value);

    Object getUserAttribute(String key);

}
