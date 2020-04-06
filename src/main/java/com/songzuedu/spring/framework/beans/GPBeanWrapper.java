package com.songzuedu.spring.framework.beans;


/**
 * <p></p>
 *
 * @author gengen.wang
 **/
public class GPBeanWrapper {

    private Object wrappedInstance;

    private Class<?> wrappedClass;

    public GPBeanWrapper() {
    }

    public GPBeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

    public Object getWrappedInstance() {
        return this.wrappedInstance;
    }

    //返回代理以后得class
    //可能会是这个 $Proxy0
    public Class<?> getWrappedClass() {
        return this.wrappedInstance.getClass();
    }

}
