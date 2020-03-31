
package com.easy.apt.annotation.sp;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Retention(CLASS)
@Target({METHOD, PARAMETER})
/**
 * 设置有效时间
 */
public @interface Expired {

    int UNIT_MILLISECONDS = 1;
    int UNIT_SECONDS = UNIT_MILLISECONDS * 1000;
    int UNIT_MINUTES = UNIT_SECONDS * 60;
    int UNIT_HOURS = UNIT_MINUTES * 60;
    int UNIT_DAYS = UNIT_HOURS * 24;

    long value() default 0;

    int unit() default UNIT_MILLISECONDS;
}
