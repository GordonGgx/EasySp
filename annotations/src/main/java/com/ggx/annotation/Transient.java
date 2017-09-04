package com.ggx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jerry.Guan
 *         created by 2017/9/4
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface Transient {
}
