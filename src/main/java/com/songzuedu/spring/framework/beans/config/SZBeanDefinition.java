package com.songzuedu.spring.framework.beans.config;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 用来存储配置文件中的信息
 * 相当于保存在内存中的配置
 * </p>
 *
 * @author gengen.wang
 **/
@Data
@NoArgsConstructor
public class SZBeanDefinition {

    private String beanClassName;

    private Boolean lazyInit = Boolean.FALSE;

    private String factoryBeanName;

}
