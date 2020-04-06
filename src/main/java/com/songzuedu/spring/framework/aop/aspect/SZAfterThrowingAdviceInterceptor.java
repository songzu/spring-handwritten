package com.songzuedu.spring.framework.aop.aspect;

import com.songzuedu.spring.framework.aop.intercept.SZMethodInterceptor;
import com.songzuedu.spring.framework.aop.intercept.SZMethodInvocation;

import java.lang.reflect.Method;

/**
 * <p></p>
 *
 * @author gengen.wang
 **/
public class SZAfterThrowingAdviceInterceptor extends SZAbstractAspectAdvice implements SZAdvice, SZMethodInterceptor {

    private String throwingName;

    public SZAfterThrowingAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(SZMethodInvocation mi) throws Throwable {
        try {
            return mi.proceed();
        } catch (Throwable e) {
            invokeAdviceMethod(mi, null, e);
            throw e;
        }
    }

    public void setThrowName(String throwName) {
        this.throwingName = throwName;
    }
}
