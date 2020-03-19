
package com.easy.apt.annotation.sp;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Retention(CLASS)
@Target({TYPE, METHOD})
public @interface Apply {
}
