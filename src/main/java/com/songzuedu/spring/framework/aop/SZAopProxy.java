package com.songzuedu.spring.framework.aop;

/**
 * <p></p>
 *
 * @author gengen.wang
 **/
//默认用 JDK 动态代理
public interface SZAopProxy {

    Object getProxy();

    Object getProxy(ClassLoader classLoader);

}
