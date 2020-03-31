
package com.easy.apt.annotation.sp;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Retention(CLASS)
@Target(METHOD)
/**
 * 清除所有数据
 */
public @interface Clear {
}
