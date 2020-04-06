package com.songzuedu.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * <p>页面交互</p>
 *
 * @author gengen.wang
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SZController {
    String value() default "";
}
