package com.easy.store.dao;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.easy.store.bean.CacheDo;
import com.easy.store.bean.CacheDo_;

import javax.inject.Inject;

import io.objectbox.Box;

public class CacheDao extends BaseDao {

    Box<CacheDo> cacheDoBox;

    @Inject
    public CacheDao() {
        if (boxStore != null) {
            cacheDoBox = boxStore.boxFor(CacheDo.class);
        }
    }

    private CacheDo query(String key) {
        if (!TextUtils.isEmpty(key) && cacheDoBox != null) {
            return cacheDoBox.query().equal(CacheDo_.key, key).build().findFirst();
        }
        return null;
    }

    private boolean put(String key, Object value) {
        CacheDo cacheDo = query(key);
        if (cacheDo != null) {
            cacheDo.setValue(formatToString(value));
        } else {
            cacheDo = new CacheDo();
            cacheDo.setKey(key);
            cacheDo.setValue(formatToString(value));
        }
        if (cacheDoBox != null) {
            cacheDoBox.put(cacheDo);
            return true;
        }
        return false;
    }

    private String formatToString(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else if (value != null) {
            return JSON.toJSONString(value);
        }
        return null;
    }

    public int read(String key, int defaultValue) {
        CacheDo cacheDo = query(key);
        if (cacheDo != null && !TextUtils.isEmpty(cacheDo.getValue())) {
            try {
                return Integer.valueOf(cacheDo.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    public float read(String key, float defaultValue) {
        CacheDo cacheDo = query(key);
        if (cacheDo != null && !TextUtils.isEmpty(cacheDo.getValue())) {
            try {
                return Float.valueOf(cacheDo.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    public long read(String key, long defaultValue) {
        CacheDo cacheDo = query(key);
        if (cacheDo != null && !TextUtils.isEmpty(cacheDo.getValue())) {
            try {
                return Long.valueOf(cacheDo.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    public boolean read(String key, boolean defaultValue) {
        CacheDo cacheDo = query(key);
        if (cacheDo != null && !TextUtils.isEmpty(cacheDo.getValue())) {
            try {
                return Boolean.valueOf(cacheDo.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    public String read(String key, String defaultValue) {
        CacheDo cacheDo = query(key);
        if (cacheDo != null && cacheDo.getValue() != null) {
            try {
                return cacheDo.getValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    public <T> T read(String key, Class<T> clazz) {
        CacheDo cacheDo = query(key);
        if (cacheDo != null && !TextUtils.isEmpty(cacheDo.getValue())) {
            try {
                String value = cacheDo.getValue();
                return JSON.parseObject(value, clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean write(String key, int value) {
        return put(key, String.valueOf(value));
    }

    public boolean write(String key, float value) {
        return put(key, String.valueOf(value));
    }

    public boolean write(String key, long value) {
        return put(key, String.valueOf(value));
    }

    public boolean write(String key, boolean value) {
        return put(key, String.valueOf(value));
    }

    public boolean write(String key, String value) {
        return put(key, value);
    }

    public boolean write(String key, Object value) {
        return put(key, value);
    }

    public boolean remove(String key) {
        CacheDo cacheDo = query(key);
        if (cacheDo != null) {
            cacheDoBox.remove(cacheDo);
            return true;
        }
        return false;
    }

    public boolean clearAll() {
        if (cacheDoBox != null) {
            cacheDoBox.removeAll();
            return true;
        }
        return false;
    }
}
