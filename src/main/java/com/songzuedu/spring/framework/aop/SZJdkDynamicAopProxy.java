package com.songzuedu.spring.framework.aop;

import com.songzuedu.spring.framework.aop.intercept.SZMethodInvocation;
import com.songzuedu.spring.framework.aop.support.SZAdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * <p></p>
 *
 * @author gengen.wang
 **/
public class SZJdkDynamicAopProxy implements SZAopProxy, InvocationHandler {

    private SZAdvisedSupport advised;

    public SZJdkDynamicAopProxy(SZAdvisedSupport config) {
        this.advised = config;
    }

    @Override
    public Object getProxy() {
        return getProxy(this.advised.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, advised.getTargetClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> interceptorsAndDynamicInterceptionAdvice = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, this.advised.getTargetClass());
        SZMethodInvocation invocation = new SZMethodInvocation(proxy, this.advised.getTarget(), method, args, this.advised.getTargetClass(), interceptorsAndDynamicInterceptionAdvice);
        return invocation.proceed();
    }
}
