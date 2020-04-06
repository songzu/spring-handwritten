package com.songzuedu.spring.framework.context.support;

/**
 * <p>IOC容器实现的顶层设计</p>
 *
 * @author gengen.wang
 **/
public abstract class SZAbstractApplicationContext {

    //提供给子类重写
    public void refresh() throws Exception {}

}
