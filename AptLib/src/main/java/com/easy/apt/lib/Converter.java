
package com.easy.apt.lib;

import java.lang.reflect.Type;

public interface Converter<F, T> {

    T convert(F value);

    interface Factory {

        <F> Converter<F, String> fromType(Type fromType);
        <T> Converter<String, T> toType(Type toType);
    }
}
