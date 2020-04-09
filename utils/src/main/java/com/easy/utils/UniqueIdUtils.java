package com.easy.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 可能会遇到无权限的异常, 这里直接吃掉,然后抛出 @UniqueException
 */
public class UniqueIdUtils {

    public enum DEVICES_INFO {
        IMEI,
        MAC,
        SERIAL,
        ANDROID_ID
    }

    //private static final String SP_NAME = "GUID";
    static Map<String, String> result = new HashMap<>();


    /**
     * @param info enum
     * @return device info
     */
    public static String getDeviceInfo(Context context, DEVICES_INFO info) {
        return getDeviceInfo(context).get(info.name());
    }

    /**
     * @return info map
     */
    public static Map<String, String> getDeviceInfo(Context context) {
        if (!result.keySet().isEmpty()) {
            return result;
        }
        result = fetch(context);
        try {
            String imei = getIMEI(context);
            result.put(DEVICES_INFO.IMEI.name(), imei);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String mac = getMacId(context);
            result.put(DEVICES_INFO.MAC.name(), mac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String androidId = getAndroidId(context);
            result.put(DEVICES_INFO.ANDROID_ID.name(), androidId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String serial = getSerialNumber(context);
            result.put(DEVICES_INFO.SERIAL.name(), serial);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果全部都没有获取到 设置uuid
        if (result.keySet().isEmpty()) {
            result.put(DEVICES_INFO.ANDROID_ID.name(), getUUID());
        }
        save(context, result);
        return result;
    }

    private static void save(Context context, Map<String, String> result) {
        if (result == null || result.isEmpty()) {
            return;
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);// context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        for (String key : result.keySet()) {
            String value = result.get(key);
            sp.edit().putString(key, value).apply();
        }
    }

    private static Map<String, String> fetch(Context context) {
        Map<String, String> map = new HashMap<>();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);//context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        for (DEVICES_INFO info : DEVICES_INFO.values()) {
            String value = sp.getString(info.name(), "");
            map.put(info.name(), value);
        }
        return map;
    }

    @SuppressLint({"HardwareIds", "MissingPermission"})
    private static String getIMEI(Context context) throws Exception {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId().toLowerCase();
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return "";
    }

    private static boolean validMac(String address) {
        return !EmptyUtils.isEmpty(address)
                && !StringUtils.equals(address, "00:00:00:00:00:00")
                && !StringUtils.equals(address, "ff:ff:ff:ff:ff:ff")
                && !StringUtils.equals(address, "02:00:00:00:00:00");
    }

    private static String getMacId(Context context) throws Exception {
        try {
            WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifi != null) {
                @SuppressLint("MissingPermission") WifiInfo info = wifi.getConnectionInfo();
                String address = info.getMacAddress();
                if (validMac(address)) {
                    throw new Exception("mac addr is empty");
                }
                return address.toLowerCase();
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return "00000000";
    }

    private static String getAndroidId(Context context) throws Exception {
        try {
            String androidId = Settings.System.getString(
                    context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (EmptyUtils.isEmpty(androidId)
                    || StringUtils.equals(androidId, "9774d56d682e549c")) {
                throw new Exception("bug androidId " + String.valueOf(androidId));
            }
            return androidId.toLowerCase();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * 这里得到的值不一定唯一,同批次机型有可能一致
     *
     * @param context context
     * @return str
     * @throws Exception
     */
    private static String getSerialNumber(Context context) throws Exception {
        try {
            String result = "35" + //we make this look like a valid IMEI
                    Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                    Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                    Build.DISPLAY.length() % 10 + Build.VERSION.RELEASE.length() % 10 +
                    Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                    Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                    Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                    Build.USER.length() % 10; //13 digits
            return result.toLowerCase();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().toLowerCase();
    }
}
