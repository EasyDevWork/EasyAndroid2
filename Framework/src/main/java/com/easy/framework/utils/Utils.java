package com.easy.framework.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Utils {
    public static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean isEmpty(Object obj) {
        return obj == null || obj.toString().length() == 0;
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }

    public static boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static Double formatDouble(String str) {
        if (isNotEmpty(str)) {
            try {
                return Double.valueOf(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0d;
    }
    /**
     * 提供精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示需要精确到小数点以后几位
     * @return 两个参数的商
     */
    public static String div(String v1, String v2, int scale) {
        if (scale < 0) {
            scale = 0;
        }
        boolean check = checkData(v1);
        if (!check) {
            v1 = "0.0000";
        }
        check = checkData(v2);
        if (!check) {
            v2 = "1.0000";
        } else if (Utils.formatDouble(v2) == 0) {
            v2 = "1.0000";
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_DOWN).toString();
    }

    public static boolean checkData(String value) {
        try {
            if (TextUtils.isEmpty(value) || TextUtils.isEmpty(value.trim())) {
                return false;
            }
            Float.valueOf(value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String mul(String value1, String value2,int scale) {
        boolean check = checkData(value1);
        if (!check) {
            value1 = "0.0000";
        }
        check = checkData(value2);
        if (!check) {
            value2 = "0.0000";
        }
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.multiply(b2).setScale(scale, BigDecimal.ROUND_HALF_DOWN).toString();
    }

    public static String add(String value1, String value2, int scale) {
        boolean check = checkData(value1);
        if (!check) {
            value1 = "0.0000";
        }
        check = checkData(value2);
        if (!check) {
            value2 = "0.0000";
        }
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.add(b2).setScale(scale, BigDecimal.ROUND_HALF_DOWN).toString();
    }
    /**
     * 比较大小
     *
     * @param v1 被比较数
     * @param v2 比较数
     * @return 如果v1 大于v2 则 返回true 否则false
     */
    public static boolean compare(String v1, String v2) {
        if (TextUtils.isEmpty(v1) || ".".equals(v1)) {
            v1 = "0";
        }
        if (TextUtils.isEmpty(v2) || ".".equals(v2)) {
            v2 = "0";
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        try {
            int bj = b1.compareTo(b2);
            if (bj > 0 || bj == 0)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 移除map中空key或者value空值
     *
     * @param map
     */
    public static void removeNullEntry(Map map) {
        removeNullKey(map);
        removeNullValue(map);
    }

    /**
     * 移除map的空key
     *
     * @param map
     * @return
     */
    public static void removeNullKey(Map map) {
        Set set = map.keySet();
        for (Iterator iterator = set.iterator(); iterator.hasNext(); ) {
            Object obj = iterator.next();
            remove(obj, iterator);
        }
    }

    /**
     * 移除map中的value空值
     *
     * @param map
     * @return
     */
    public static void removeNullValue(Map map) {
        Set set = map.keySet();
        for (Iterator iterator = set.iterator(); iterator.hasNext(); ) {
            Object obj = iterator.next();
            Object value = map.get(obj);
            remove(value, iterator);
        }
    }

    /**
     * 移除map中的空值
     * <p>
     * Iterator 是工作在一个独立的线程中，并且拥有一个 mutex 锁。
     * Iterator 被创建之后会建立一个指向原来对象的单链索引表，当原来的对象数量发生变化时，这个索引表的内容不会同步改变，
     * 所以当索引指针往后移动的时候就找不到要迭代的对象，所以按照 fail-fast 原则 Iterator 会马上抛出 java.util.ConcurrentModificationException 异常。
     * 所以 Iterator 在工作的时候是不允许被迭代的对象被改变的。
     * 但你可以使用 Iterator 本身的方法 remove() 来删除对象， Iterator.remove() 方法会在删除当前迭代对象的同时维护索引的一致性。
     *
     * @param obj
     * @param iterator
     */
    private static void remove(Object obj, Iterator iterator) {
        if (obj instanceof String) {
            String str = (String) obj;
            if (isEmpty(str)) {  //过滤掉为null和""的值 主函数输出结果map：{2=BB, 1=AA, 5=CC, 8=  }
//            if("".equals(str.trim())){  //过滤掉为null、""和" "的值 主函数输出结果map：{2=BB, 1=AA, 5=CC}
                iterator.remove();
            }

        } else if (obj instanceof Collection) {
            Collection col = (Collection) obj;
            if (col == null || col.isEmpty()) {
                iterator.remove();
            }

        } else if (obj instanceof Map) {
            Map temp = (Map) obj;
            if (temp == null || temp.isEmpty()) {
                iterator.remove();
            }

        } else if (obj instanceof Object[]) {
            Object[] array = (Object[]) obj;
            if (array == null || array.length <= 0) {
                iterator.remove();
            }
        } else {
            if (obj == null) {
                iterator.remove();
            }
        }
    }

    public static String buildString(Object... str) {
        int size = str.length;
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < size; ++i) {
            if (str[i] != null) {
                builder.append(str[i]);
            }
        }
        return builder.toString();
    }

    public static String getApplicationId(Context context) throws IllegalArgumentException {
        if (context == null) {
            return "";
        }
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return applicationInfo.metaData.getString("APP_ID");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getAuthority(Context appContext) {
        return getApplicationId(appContext) + ".provider";
    }
}
