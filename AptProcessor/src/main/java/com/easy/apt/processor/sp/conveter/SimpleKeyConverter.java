
package com.easy.apt.processor.sp.conveter;

public class SimpleKeyConverter implements KeyConverter {

    @Override
    public String convert(String key) {
        if (key != null && (key.startsWith("set") || key.startsWith("get") || key.startsWith("put"))) {
            return key.substring(3).toLowerCase();
        }
        if (key != null && key.startsWith("is")) {
            return key.substring(2).toLowerCase();
        }
        if (key != null && (key.startsWith("remove") || key.startsWith("delete"))){
            return key.substring(6).toLowerCase();
        }
        return key.toLowerCase();
    }
}
