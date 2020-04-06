package com.songzuedu.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * <p>请求参数映射</p>
 *
 * @author gengen.wang
 **/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SZRequestParam {
    String value() default "";
}
