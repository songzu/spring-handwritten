package com.songzuedu.spring.framework.aop.aspect;

import com.songzuedu.spring.framework.aop.intercept.SZMethodInterceptor;
import com.songzuedu.spring.framework.aop.intercept.SZMethodInvocation;

import java.lang.reflect.Method;

/**
 * <p></p>
 *
 * @author gengen.wang
 **/
public class SZAfterReturningAdviceInterceptor extends SZAbstractAspectAdvice implements SZAdvice, SZMethodInterceptor {

    private SZJoinPoint joinPoint;

    public SZAfterReturningAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(SZMethodInvocation mi) throws Throwable {
        Object retVal = mi.proceed();
        this.joinPoint = mi;
        this.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());

        return retVal;
    }

    private void afterReturning(Object retVal, Method method, Object[] args, Object aThis) throws Throwable {
        super.invokeAdviceMethod(this.joinPoint, retVal, null);
    }
}
