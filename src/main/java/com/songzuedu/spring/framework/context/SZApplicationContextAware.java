package com.songzuedu.spring.framework.context;

/**
 * <p>
 * 通过解耦方式获得IOC容器顶层设计
 * 后面将通过一个监听器去扫描所有的类
 * 只要实现了此接口，将自动调用setApplicationContext，从而将IOC容器注入到目标类中
 * </p>
 *
 * @author gengen.wang
 **/
public interface SZApplicationContextAware {

    void setApplicationContext(SZApplicationContext applicationContext);

}
