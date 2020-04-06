package com.songzuedu.spring.framework.beans.support;



import com.songzuedu.spring.framework.beans.config.SZBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * <p>对配置文件进行查找、读取、解析</p>
 *
 * @author gengen.wang
 **/
public class SZBeanDefinitionReader {

    private List<String> registyBeanClasses = new ArrayList<>();

    private Properties config = new Properties();

    //固定配置文件中的 key，相对于 xml 的规范
    private final String SCAN_PACKAGE = "scanPackage";


    public SZBeanDefinitionReader(String... locations) {
        //通过url定位找到其所对应的文件，然后转换为文件流
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:", ""));

        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    /**
     * 扫描类信息
     *
     * @param scanPackage
     */
    private void doScanner(String scanPackage) {
        //转换为文件路径，实际上就是把.替换为/
        //web使用
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        if (url == null) {
            //单元测试使用
            url = this.getClass().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        }

        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String className = (scanPackage + "." + file.getName().replace(".class", ""));
                registyBeanClasses.add(className);
            }
        }
    }

    /**
     * 获取配置信息
     *
     * @return
     */
    public Properties getConfig() {
        return this.config;
    }

    /**
     * 把配置文件中扫描到的所有配置信息，转换为GPBeanDefinition对象，以便于之后IOC操作方便
     *
     * @return
     */
    public List<SZBeanDefinition> loadBeanDefinitions() {
        List<SZBeanDefinition> result = new ArrayList<>();

        try {
            for (String className : registyBeanClasses) {
                Class<?> beanClass = Class.forName(className);
                //如果是一个接口，不能实例化
                //用它的实现类实例化
                if (beanClass.isInterface()) {
                    continue;
                }

                //beanName有三种情况：
                //1.默认是类名首字母小写
                //2.自定义名字
                //3.接口注入
                //byName
                result.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()), beanClass.getName()));
                //byTyep
                result.add(doCreateBeanDefinition(beanClass.getName(), beanClass.getName()));

                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> i : interfaces) {
                    //如果是多个实现类，只能覆盖
                    //这个时候，可以自定义名字
                    result.add(doCreateBeanDefinition(i.getName(), beanClass.getName()));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 把每一个配置信息解析成一个BeanDefinition
     *
     * @param factoryBeanName
     * @param beanClassName
     * @return
     */
    private SZBeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {
        SZBeanDefinition beanDefinition = new SZBeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

    /**
     * 约定：类遵循驼峰命名法
     *
     * @param simpleName
     * @return
     */
    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        //大小写字母的 ASCII 码相差 32
        //大写字母的 ASCII 码要小于小写字母的 ASCII 码
        //对 char 做算学运算，实际上就是对 ASCII 码做算学运算
        chars[0] += 32;
        return String.valueOf(chars);
    }


}
