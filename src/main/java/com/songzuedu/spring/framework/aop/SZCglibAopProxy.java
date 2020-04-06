package com.songzuedu.spring.framework.aop;

import com.songzuedu.spring.framework.aop.support.SZAdvisedSupport;

/**
 * <p></p>
 *
 * @author gengen.wang
 **/
public class SZCglibAopProxy implements SZAopProxy {

    public SZCglibAopProxy(SZAdvisedSupport config) {
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
