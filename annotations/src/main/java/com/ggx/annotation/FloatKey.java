package com.ggx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jerry.Guan
 *         created by 2017/9/2
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface FloatKey {

    String name() default "";
    float value() default 0f;

}
