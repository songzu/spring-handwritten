package com.songzuedu.spring.framework.aop.aspect;


import java.lang.reflect.Method;

/**
 * <p></p>
 *
 * @author gengen.wang
 **/
public class SZAbstractAspectAdvice implements SZAdvice {

    private Method aspectMethod;

    private Object aspectTarget;

    public SZAbstractAspectAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }

    public Object invokeAdviceMethod(SZJoinPoint joinPoint, Object returnValue, Throwable tx) throws Throwable {
        Class<?>[] paramTypes = this.aspectMethod.getParameterTypes();
        if (paramTypes == null || paramTypes.length == 0) {
            return this.aspectMethod.invoke(this.aspectTarget);
        } else {
            Object[] args = new Object[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                if (paramTypes[i] == SZJoinPoint.class) {
                    args[i] = joinPoint;
                } else if (paramTypes[i] == Throwable.class) {
                    args[i] = tx;
                } else if (paramTypes[i] == Object.class) {
                    args[i] = returnValue;
                }
            }
            return this.aspectMethod.invoke(this.aspectTarget, args);
        }
    }

}
