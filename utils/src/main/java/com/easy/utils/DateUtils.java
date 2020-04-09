package com.easy.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期格式
 */
public class DateUtils {

    /**
     * 把 00:00:00 格式转成时间戳
     *
     * @param formatTime 00:00:00 时间格式
     * @return 时间戳(毫秒)
     */
    public static int getIntTime(String formatTime) {
        if (TextUtils.isEmpty(formatTime)) {
            return 0;
        }

        String[] tmp = formatTime.split(":");
        if (tmp.length < 3) {
            return 0;
        }
        int second = Integer.parseInt(tmp[0]) * 3600 + Integer.parseInt(tmp[1]) * 60 + Integer.parseInt(tmp[2]);
        return second * 1000;
    }

    /**
     * 把时间戳转换成 00:00:00 格式
     *
     * @param timeMs 时间戳
     * @return 00:00:00 时间格式
     */
    public static String getStringTime(long timeMs) {
        StringBuilder formatBuilder = new StringBuilder();
        java.util.Formatter formatter = new java.util.Formatter(formatBuilder, Locale.getDefault());
        long totalSeconds = timeMs / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        formatBuilder.setLength(0);
        return formatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
    }

    /**
     * 格式化毫秒数为 xx:xx:xx这样的时间格式。
     *
     * @param ms 毫秒数
     * @return 格式化后的字符串
     */
    public static String formatMs(long ms) {
        int seconds = (int) (ms / 1000);
        int finalSec = seconds % 60;
        int finalMin = seconds / 60 % 60;
        int finalHour = seconds / 3600;

        StringBuilder msBuilder = new StringBuilder("");
        if (finalHour > 9) {
            msBuilder.append(finalHour).append(":");
        } else if (finalHour > 0) {
            msBuilder.append("0").append(finalHour).append(":");
        }

        if (finalMin > 9) {
            msBuilder.append(finalMin).append(":");
        } else if (finalMin > 0) {
            msBuilder.append("0").append(finalMin).append(":");
        } else {
            msBuilder.append("00").append(":");
        }

        if (finalSec > 9) {
            msBuilder.append(finalSec);
        } else if (finalSec > 0) {
            msBuilder.append("0").append(finalSec);
        } else {
            msBuilder.append("00");
        }

        return msBuilder.toString();
    }

    /**
     * 把时间戳转换成 00:00:00 格式
     *
     * @param seconds
     * @return 00:00:00
     */
    public static String formatDate(long seconds) {
        String finalStr = "";
        long mills = seconds * 1000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        finalStr += (hour < 10 ? "0" + hour : hour) + ":";
        int minute = calendar.get(Calendar.MINUTE);
        finalStr += (minute < 10 ? "0" + minute : minute) + ":";
        int second = calendar.get(Calendar.SECOND);
        finalStr += (second < 10 ? "0" + second : second);
        return finalStr;
    }

    /**
     * 字符串转时间戳
     *
     * @param timeString
     * @return
     */
    public static Long strToTimestamp(String timeString, int parameters) {
        SimpleDateFormat simpleDateFormat = getSimpleDate(parameters);
        Date d;
        try {
            d = simpleDateFormat.parse(timeString);
            return d.getTime() + TimeZone.getDefault().getRawOffset();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static long dateToTimestamp(String timeString, int parameters, TimeZone timeZone) {
        SimpleDateFormat simpleDateFormat = getSimpleDate(parameters);
        try {
            Date d = simpleDateFormat.parse(timeString);
            return d.getTime() + timeZone.getRawOffset();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static SimpleDateFormat getSimpleDate(int parameters) {
        SimpleDateFormat formatter = null;
        switch (parameters) {
            case 0:
                formatter = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
                break;
            case 1:
                formatter = new SimpleDateFormat("yyyy.MM.dd;HH:mm", Locale.getDefault());
                break;
            case 2:
                formatter = new SimpleDateFormat("MM.dd;HH:mm", Locale.getDefault());
                break;
            case 3:
                formatter = new SimpleDateFormat("yyyy.MM.dd;HH:mm:ss", Locale.getDefault());
                break;
            case 4:
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                break;
            case 5:
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                break;
            case 6:
                formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                break;
            case 7:
                formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                break;
            case 8:
                formatter = new SimpleDateFormat("yyyyMM", Locale.getDefault());
                break;
            case 9:
                formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
                break;
            case 10:
                formatter = new SimpleDateFormat("dd", Locale.getDefault());
                break;
            case 11:
                formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                break;
            case 12:
                formatter = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
                break;
            case 13:
                formatter = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.getDefault());
                break;
            case 14:
                formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                break;
            case 15:
                formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                break;

            case 16:
                formatter = new SimpleDateFormat("MM.dd", Locale.getDefault());
                break;
        }
        return formatter;
    }

    /**
     * 获取两个日期所差的天数
     *
     * @param time1 大的时间
     * @param time2 小的时间
     * @return
     */
    public static long[] getTwoTimeOffset(long time1, long time2) {
        SimpleDateFormat df = getSimpleDate(4);
        try {
            Date d1 = df.parse(getDateToString(time1, 4)); //
            Date d2 = df.parse(getDateToString(time2, 4)); //
            long diff = d1.getTime() - d2.getTime(); // 两个时间差,精确到毫秒

            long day = diff / (1000 * 60 * 60 * 24); // 以天数为单位取整
            long hour = (diff / (60 * 60 * 1000) - day * 24); // 以小时为单位取整
            long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60); // 以分钟为单位取整
            return new long[]{day, hour, min};
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    public static String getDateToString(long time, int parameters) {
        SimpleDateFormat formatter = getSimpleDate(parameters);
        if (formatter != null) {
            Date date = new Date(time);
            return formatter.format(date);
        }
        return "";
    }
    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTimeDate(int parameters) {
        SimpleDateFormat formatter = getSimpleDate(parameters);
        Date curDate = getCurrentDate();// 获取当前时间
        if (formatter != null) {
            return formatter.format(curDate);
        }
        return "";
    }

    public static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());// 获取当前时间
    }


    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime   当前时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 时间解析成long
     *
     * @param date 2019-10-09T10:16:10.541809+08:00
     * @return
     */
    public static long parse(String date) {
        if (date != null && date.contains("+")) {
            String[] split1 = date.split("\\+");
            String time = split1[0];
            return DateUtils.getTimeStamp(time);
        }
        return 0;
    }
    //2019-08-13T19:38:10.332471+08:00
    public static long getTimeStamp(String time) {
        if (time != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
            try {
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
                Date date = simpleDateFormat.parse(time);
                long ts = date.getTime();
                return ts;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 00000;
    }
}
