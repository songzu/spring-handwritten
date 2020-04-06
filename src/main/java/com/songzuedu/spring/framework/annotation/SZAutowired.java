package com.songzuedu.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * <p>自动注入</p>
 *
 * @author gengen.wang
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SZAutowired {
    String value() default "";

}
