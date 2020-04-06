package com.songzuedu.spring.framework.aop.intercept;

/**
 * <p></p>
 *
 * @author gengen.wang
 **/
public interface SZMethodInterceptor {

    Object invoke(SZMethodInvocation invocation) throws Throwable;

}
