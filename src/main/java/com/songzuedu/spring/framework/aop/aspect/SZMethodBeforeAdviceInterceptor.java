package com.songzuedu.spring.framework.aop.aspect;

import com.songzuedu.spring.framework.aop.intercept.SZMethodInterceptor;
import com.songzuedu.spring.framework.aop.intercept.SZMethodInvocation;

import java.lang.reflect.Method;

/**
 * <p></p>
 *
 * @author gengen.wang
 **/
public class SZMethodBeforeAdviceInterceptor extends SZAbstractAspectAdvice
        implements SZAdvice, SZMethodInterceptor {

    private SZJoinPoint joinPoint;

    public SZMethodBeforeAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    private void before(Method method, Object[] args, Object target) throws Throwable {

        super.invokeAdviceMethod(this.joinPoint, null, null);
    }

    @Override
    public Object invoke(SZMethodInvocation mi) throws Throwable {
        //从被织入的代码中才能拿到，JoinPoint
        this.joinPoint = mi;
        before(mi.getMethod(), mi.getArguments(), mi.getThis());

        return mi.proceed();
    }

}
