
package com.easy.apt.processor.sp.conveter;

import javax.lang.model.type.TypeMirror;


public interface ValueConverter {

    String convert(TypeMirror type, String[] value);
}
