package com.songzuedu.spring.framework.context;


import com.songzuedu.spring.framework.annotation.SZAutowired;
import com.songzuedu.spring.framework.annotation.SZController;
import com.songzuedu.spring.framework.annotation.SZService;
import com.songzuedu.spring.framework.aop.SZAopProxy;
import com.songzuedu.spring.framework.aop.SZCglibAopProxy;
import com.songzuedu.spring.framework.aop.SZJdkDynamicAopProxy;
import com.songzuedu.spring.framework.aop.config.SZAopConfig;
import com.songzuedu.spring.framework.aop.support.SZAdvisedSupport;
import com.songzuedu.spring.framework.beans.GPBeanWrapper;
import com.songzuedu.spring.framework.beans.config.SZBeanDefinition;
import com.songzuedu.spring.framework.beans.config.SZBeanPostProcessor;
import com.songzuedu.spring.framework.beans.support.SZBeanDefinitionReader;
import com.songzuedu.spring.framework.beans.support.SZDefaultListableBeanFactory;
import com.songzuedu.spring.framework.core.SZBeanFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>IOC、DI、MVC、AOP</p>
 *
 * @author gengen.wang
 **/
public class SZApplicationContext extends SZDefaultListableBeanFactory implements SZBeanFactory {

    private String[] configLoactions;

    private SZBeanDefinitionReader reader;

    //单例的IOC容器缓存
    //用来保证注册式单例的容器
    private Map<String, Object> singletonObjects = new HashMap<>();

    //通用的IOC容器
    //用来存储所有的被代理过的对象
    private Map<String, GPBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();

    public SZApplicationContext(String... configLocationgs) {
        this.configLoactions = configLocationgs;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void refresh() throws Exception {
        //1.定位：定位文件为准
        reader = new SZBeanDefinitionReader(configLoactions);

        //2.加载配置文件：扫描相关的类，把他们封装成BeanDefinition
        List<SZBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

        //3.注册：把配置信息放到容器里面(伪IOC容器)
        doRegisterBeanDefinition(beanDefinitions);

        //4.把不是延迟加载的类，都提前初始化
        doAutowirted();

    }

    /**
     * 只处理非延迟加载的情况
     */
    private void doAutowirted() {
        for (Map.Entry<String, SZBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinitionEntry.getValue().getLazyInit()) {
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doRegisterBeanDefinition(List<SZBeanDefinition> beanDefinitions) throws Exception {
        for (SZBeanDefinition beanDefinition : beanDefinitions) {
            if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception("The " + beanDefinition.getFactoryBeanName() + "is exists!");
            }

            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
        //到这里容器初始化完成
    }

    /**
     * byType
     *
     * @param beanClass
     * @return
     */
    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getName());
    }


    /**
     * byName
     * <p>
     * 依赖注入，从这里开始，通过读取BeanDefinition中的信息
     * 然后，通过反射机制创建一个实例并返回
     * Spring的做法：不会把最原始的对象放进去，会用一个BeanWrapper进行一次包装
     * <p>
     * 装饰器模式：
     * 1.保留原有的OOP关系
     * 2.需要对他进行扩展，增强(为了以后AOP打基础)
     *
     * @param beanName
     * @return
     */
    @Override
    public Object getBean(String beanName) throws Exception {
        SZBeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        Object instance = null;

        //生成通知事件
        //这个逻辑还不严谨，参考Spring源码
        // TODO: 2020/2/28  工厂模式 + 策略模式
        SZBeanPostProcessor postProcessor = new SZBeanPostProcessor();

        //在实例初始化以前调用一次
        postProcessor.postProcessBeforeInitialization(instance, beanName);

        instance = instantiateBean(beanDefinition);
        if (instance == null) {
            return null;
        }

        //把BeanWrapper存到IOC容器里面
        //循环依赖问题
        //class A{ B b;}
        //class B{ A a;}
        //先有鸡还是先有蛋的问题，一个方法是搞不定的，要分两次
        //1.初始化:把这个对象封装到BeanWrapper中
        GPBeanWrapper beanWrapper = new GPBeanWrapper(instance);
        //2.拿到BeanWraoper之后，把BeanWrapper保存到IOC容器中去
        this.factoryBeanInstanceCache.put(beanName, beanWrapper);
        //在实例初始化以后调用一次 
        postProcessor.postProcessAfterInitialization(instance, beanName);
        //3.注入
        populateBean(beanName, instance);

        return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
    }

    /**
     * 注入
     *
     * @param beanName
     * @param instance
     */
    private void populateBean(String beanName, Object instance) {
        Class<?> clazz = instance.getClass();
        //判断只有加了注解的类，才执行依赖注入
        if (!(clazz.isAnnotationPresent(SZController.class) || clazz.isAnnotationPresent(SZService.class))) {
            return;
        }
        //获得所有的fields
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(SZAutowired.class)) {
                continue;
            }
            SZAutowired autowired = field.getAnnotation(SZAutowired.class);
            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)) {
                autowiredBeanName = field.getType().getName();
            }
            //强制访问
            field.setAccessible(Boolean.TRUE);
            try {
                //为什么会为NULL？// TODO: 2020/2/28
                if (this.factoryBeanInstanceCache.get(autowiredBeanName) == null) {
                    continue;
                }
                field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 传一个BeanDefinition，就返回一个实例bean
     *
     * @param beanDefinition
     * @return
     */
    private Object instantiateBean(SZBeanDefinition beanDefinition) {
        //1、拿到要实例化的对象的类名
        String className = beanDefinition.getBeanClassName();
        //2、反射实例化，得到一个对象
        Object instance = null;
        try {
            //因为根据Class才能确定一个类是否有实例
            if (this.singletonObjects.containsKey(className)) {
                instance = this.singletonObjects.get(className);
            } else {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();

                //AOP
                SZAdvisedSupport config = instantionAopConfig(beanDefinition);
                config.setTarget(instance);
                config.setTargetClass(clazz);
                //符合PointCut的规则的话，闯将代理对象
                if (config.pointCutMatche()) {
                    instance = createProxy(config).getProxy();
                }

                this.singletonObjects.put(className, instance);
                this.singletonObjects.put(beanDefinition.getFactoryBeanName(), instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }

    private SZAopProxy createProxy(SZAdvisedSupport config) {
        Class<?> targetClass = config.getTargetClass();
        if (targetClass.getInterfaces().length > 0) {
            return new SZJdkDynamicAopProxy(config);
        }

        return new SZCglibAopProxy(config);
    }

    private SZAdvisedSupport instantionAopConfig(SZBeanDefinition gpBeanDefinition) {
        SZAopConfig config = new SZAopConfig();
        config.setPointCut(this.reader.getConfig().getProperty("pointCut"));
        config.setAspectClass(this.reader.getConfig().getProperty("aspectClass"));
        config.setAspectBefore(this.reader.getConfig().getProperty("aspectBefore"));
        config.setAspectAfter(this.reader.getConfig().getProperty("aspectAfter"));
        config.setAspectAfterThrow(this.reader.getConfig().getProperty("aspectAfterThrow"));
        config.setAspectAfterThrowingName(this.reader.getConfig().getProperty("aspectAfterThrowingName"));

        return new SZAdvisedSupport(config);
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    public Properties getConfig() {
        return this.reader.getConfig();
    }

}
