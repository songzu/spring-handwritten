package com.songzuedu.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * <p>请求 url</p>
 *
 * @author gengen.wang
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SZRequestMapping {
    String value() default "";
}
