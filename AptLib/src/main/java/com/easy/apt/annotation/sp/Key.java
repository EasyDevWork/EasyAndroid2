
package com.easy.apt.annotation.sp;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Retention(CLASS)
@Target({METHOD, PARAMETER})
/**
 * 用来自定义键的名称，没有定义KEY，默认用方法名
 */
public @interface Key {

    String value();
}
