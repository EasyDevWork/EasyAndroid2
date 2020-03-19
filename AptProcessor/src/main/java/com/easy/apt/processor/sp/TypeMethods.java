
package com.easy.apt.processor.sp;

import java.util.HashMap;

import javax.lang.model.type.TypeMirror;

public class TypeMethods {

    public static final String INT = "Int";
    public static final String LONG = "Long";
    public static final String FLOAT = "Float";
    public static final String BOOLEAN = "Boolean";
    public static final String STRING = "String";
    public static final String STRINGSET = "StringSet";
    public static final String ALL = "All";

    /**
     * mPreferences.getAll();
     * mPreferences.getBoolean();
     * mPreferences.getFloat();
     * mPreferences.getInt();
     * mPreferences.getLong();
     * mPreferences.getString();
     * mPreferences.getStringSet();
     */
    private static final HashMap<String, String> METHOD_MAP = new HashMap<String, String>() {
        {
            put("int", INT);
            put("long", LONG);
            put("float", FLOAT);
            put("boolean", BOOLEAN);
            put("java.lang.String", STRING);
            put("java.util.Set<java.lang.String>", STRINGSET);
            put("java.util.Map<String, ?>", ALL);
        }
    };

    public static String typeName(TypeMirror type) {
        return METHOD_MAP.get(type.toString());
    }

    public static String getterMethod(TypeMirror type) {
        String method = METHOD_MAP.get(type.toString());
        if (method != null) {
            return "get" + method;
        }
        return null;
    }

    public static String setterMethod(TypeMirror type) {
        String method = METHOD_MAP.get(type.toString());
        if (method != null) {
            return "put" + method;
        }
        return null;
    }
}
