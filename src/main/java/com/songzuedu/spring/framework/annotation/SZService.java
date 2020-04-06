package com.songzuedu.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * <p>业务逻辑,注入接口</p>
 *
 * @author gengen.wang
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SZService {
    String value() default "";

}
