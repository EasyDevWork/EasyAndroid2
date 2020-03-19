
package com.easy.apt.lib;


import android.content.Context;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * 使用参考 https://github.com/baoyongzhang/Treasure
 */
public class SharePreference {

    private static HashMap<Key, Object> sPreferencesCache;
    private static Converter.Factory sConverterFactory;

    static {
        sPreferencesCache = new HashMap();
        sConverterFactory = new SimpleConverterFactory();
    }

    public static void setConverterFactory(Converter.Factory factory) {
        sConverterFactory = factory;
    }

    public static <T> T get(Context context, Class<T> interfaceClass) {
        return get(context, interfaceClass, null);
    }

    public static <T> T get(Context context, Class<T> interfaceClass, String id) {
        Key key = new Key();
        key.interfaceClass = interfaceClass;
        key.id = id;
        T value = (T) sPreferencesCache.get(key);
        if (value != null) {
            return value;
        }
        try {
            Class<?> aClass = Class.forName(interfaceClass.getName() + "$$SP");
            Object obj;
            if (id == null) {
                Constructor constructor = aClass.getDeclaredConstructor(Context.class, Converter.Factory.class);
                constructor.setAccessible(true);
                obj = constructor.newInstance(context, sConverterFactory);
            } else {
                Constructor constructor = aClass.getDeclaredConstructor(Context.class, Converter.Factory.class, String.class);
                constructor.setAccessible(true);
                obj = constructor.newInstance(context, sConverterFactory);
            }
            value = (T) obj;
            sPreferencesCache.put(key, value);
            return value;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static class Key {
        Class<?> interfaceClass;
        String id;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            if (interfaceClass != null ? !interfaceClass.equals(key.interfaceClass) : key.interfaceClass != null)
                return false;
            return !(id != null ? !id.equals(key.id) : key.id != null);

        }

        @Override
        public int hashCode() {
            int result = interfaceClass != null ? interfaceClass.hashCode() : 0;
            result = 31 * result + (id != null ? id.hashCode() : 0);
            return result;
        }
    }
}
