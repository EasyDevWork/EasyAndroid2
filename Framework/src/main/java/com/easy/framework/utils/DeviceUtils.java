package com.easy.framework.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.TimeZone;
import java.util.UUID;

public class DeviceUtils {

    public static boolean isStrangePhone() {
        boolean strangePhone = "mx5".equalsIgnoreCase(Build.DEVICE)
                || "Redmi Note2".equalsIgnoreCase(Build.DEVICE)
                || "Z00A_1".equalsIgnoreCase(Build.DEVICE)
                || "hwH60-L02".equalsIgnoreCase(Build.DEVICE)
                || "hermes".equalsIgnoreCase(Build.DEVICE)
                || ("V4".equalsIgnoreCase(Build.DEVICE) && "Meitu".equalsIgnoreCase(Build.MANUFACTURER))
                || ("m1metal".equalsIgnoreCase(Build.DEVICE) && "Meizu".equalsIgnoreCase(Build.MANUFACTURER));

        Log.e("isStrangePhone ", " Build.Device = " + Build.DEVICE + " , isStrange = " + strangePhone);
        return strangePhone;
    }

    public static String getAndroidId(Context context) {
        try {
            String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
            return ANDROID_ID;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static boolean isFullScreen(Activity activity) {
        return (activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }

    /**
     * 获取MAC地址
     *
     * @param context
     */

    public static String getMac(Context context) {
        return getPhoneMacAddress(context);
    }

    /**
     * 获取手机IMEI
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getImei(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String id = "";
        try {
            id = tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * 获取手机型号
     *
     * @param context
     * @return
     */
    public static String getPhoneModel(Context context) {
        return android.os.Build.MODEL;
    }

    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getProvidersIMSI(Context mContext) {
        TelephonyManager mTelephonyManager = ((TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE));
        String IMSI = "";
        try {
            IMSI = mTelephonyManager.getSubscriberId();
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return IMSI;
    }


    /**
     * 获取手机mac地址
     *
     * @param context
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String getPhoneMacAddress(Context context) {
        try {
            WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifi != null) {
                WifiInfo info = wifi.getConnectionInfo();
                String address = info.getMacAddress();
                if (!TextUtils.isEmpty(address) && !address.contains("000000000000")) {
                    return address.toLowerCase();
                } else {
                    String deviceId = getImei(context);
                    if (!TextUtils.isEmpty(deviceId) && !deviceId.contains("000000000000")) {
                        return deviceId.toLowerCase();
                    }
                    return getUUID(context);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return getUUID(context);
    }


    /**
     * 获取设备ID
     *
     * @param contxet
     * @return
     */
    public static String getDeviceId(Context contxet) {
        try {
            String ret = getMac(contxet);
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "0";


    }

    public static String getUUID(Context context) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        String identity = preference.getString("identity_android", null);
        if (identity == null) {
            //UUID uuid = new  java.util.UUID(64,64);
            //uuid.randomUUID()
            identity = UUID.randomUUID().toString();
            //identity = java.util.UUID.randomUUID().toString();
            preference.edit().putString("identity_android", identity).apply();
        }
        return identity;

    }


    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        try {
            return context.getResources().getDisplayMetrics().widthPixels;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 480;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        try {
            return context.getResources().getDisplayMetrics().heightPixels;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 800;

    }

    public static float getDeviceDensity(Context context) {
        try {
            float scale = context.getResources().getDisplayMetrics().density;
            return scale;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 480;

    }

    public static int getDeviceDensityValue(Context context) {
        //   Display display = context.getWindowManager().getDefaultDisplay();
        //   return display.getHeight();
        try {
            int scale = context.getResources().getDisplayMetrics().densityDpi;
            return scale;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 480;

    }

    public static int getMarginTopWithoutWave(Context context) {
        return DimensUtils.dp2px(context, 20);
    }


    public static String getMNC(Context context) {
        try {
            String imsi = DeviceUtils.getProvidersIMSI(context);
            if (!TextUtils.isEmpty(imsi) && imsi.length() > 3) {
                return imsi.substring(0, 3);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String getMCC(Context context) {
        try {
            String imsi = DeviceUtils.getProvidersIMSI(context);
            if (!TextUtils.isEmpty(imsi) && imsi.length() > 3) {
                return imsi.substring(0, 3);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static int getPhoneType(Context context) {
        try {
            TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            int type = telephony.getPhoneType();
            return type;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * 设备名称
     *
     * @return
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;

    }

    /**
     * 获取手机唯一码 mac_deviceid_easy
     *
     * @param context
     * @return
     */
    public static String getPhoneOnlyKey(Context context) {
        try {
            String macAddress;
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            macAddress = info.getMacAddress();
            if (macAddress == null)
                macAddress = "";

            String deviceId = getImei(context);
            if (deviceId == null)
                deviceId = "";
            String result = macAddress + "_" + deviceId + "_easy";
            if (result.length() > 32) {
                result = result.substring(0, 31);
            }
            return result.toLowerCase();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String getTimeZoneName() {
        try {
            TimeZone tz = TimeZone.getDefault();
            // String s = "TimeZone   "+tz.getDisplayName(false,
            // TimeZone.SHORT)+" Timezon id :: " +tz.getID();
            // LogUtils.dln(s);

            return tz.getDisplayName(false, TimeZone.SHORT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static boolean isInstall(Context context, String packagename) {
        boolean isInstall = false;
        try {
            PackageInfo packageInfo;
            try {
                packageInfo = context.getPackageManager().getPackageInfo(
                        packagename, PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                packageInfo = null;
                e.printStackTrace();
            }
            if (packageInfo == null) {
                isInstall = false;
            } else {
                isInstall = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isInstall;
    }

    public static int getMarginTopWithWave(Context context) {
        return DimensUtils.dp2px(context, 4);
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        try {
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            return statusBarHeight;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 20;
    }

    public static String getIpAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            // 3/4g网络
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                //  wifi网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());
                return ipAddress;
            } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                // 有限网络
                return getLocalIp();
            }
        }
        return null;
    }

    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    // 获取有限网IP
    private static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return "0.0.0.0";

    }
}
