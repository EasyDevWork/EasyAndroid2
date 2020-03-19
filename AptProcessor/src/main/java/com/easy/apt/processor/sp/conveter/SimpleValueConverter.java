
package com.easy.apt.processor.sp.conveter;


import com.easy.apt.processor.sp.TypeMethods;

import javax.lang.model.type.TypeMirror;

public class SimpleValueConverter implements ValueConverter {

    @Override
    public String convert(TypeMirror type, String[] value) {
        boolean isDefault = false;
        if (value == null || value.length < 1) {
            // not have value, return default value.
            isDefault = true;
        }
        final String typeName = TypeMethods.typeName(type);
        if (typeName == null) {
            return typeName;
        }
        switch (typeName) {
            case TypeMethods.INT:
                return isDefault ? "0" : value[0];
            case TypeMethods.FLOAT:
                return isDefault ? "0f" : value[0];
            case TypeMethods.LONG:
                return isDefault ? "0l" : value[0];
            case TypeMethods.BOOLEAN:
                return isDefault ? "false" : value[0];
            case TypeMethods.STRING:
                return isDefault ? null : "\"" + value[0] + "\"";
            case TypeMethods.STRINGSET:
                if (isDefault)
                    return null;
                StringBuilder builder = new StringBuilder();
                builder.append("new java.util.HashSet<String>(java.util.Arrays.asList(new java.lang.String[]{");
                for (int i = 0; i < value.length; i++) {
                    builder.append("\"").append(value[i]).append("\"");
                    if (i != (value.length - 1)) {
                        builder.append(",");
                    }
                }
                builder.append("}))");
                return builder.toString();
        }
        return null;
    }
}
