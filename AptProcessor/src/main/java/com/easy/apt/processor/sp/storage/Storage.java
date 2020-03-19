
package com.easy.apt.processor.sp.storage;

public interface Storage {

    void put(String key, int value);
    void put(String key, boolean value);
    void put(String key, float value);
    void put(String key, double value);
    void put(String key, long value);
    void put(String key, String value);
    void put(String key, Iterable value);
    void put(String key, Object value);
}
