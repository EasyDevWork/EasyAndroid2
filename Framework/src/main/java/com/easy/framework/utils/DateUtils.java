package com.easy.framework.utils;

import android.text.TextUtils;

import java.util.Calendar;
import java.util.Locale;

/**
 * 日期格式
 */
public class DateUtils {

    /**
     * 把 00:00:00 格式转成时间戳
     * @param formatTime    00:00:00 时间格式
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
        int second = Integer.valueOf(tmp[0]) * 3600 + Integer.valueOf(tmp[1]) * 60 + Integer.valueOf(tmp[2]);
        return second * 1000;
    }

    /**
     * 把时间戳转换成 00:00:00 格式
     * @param timeMs    时间戳
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

    public static String formatTime(int ms) {
        int totalSeconds = ms / 1000;
        int seconds = totalSeconds % 60;
        int minutes = totalSeconds / 60 % 60;
        int hours = totalSeconds / 60 / 60;
        String timeStr = "";
        if (hours > 9) {
            timeStr += hours + ":";
        } else if (hours > 0) {
            timeStr += "0" + hours + ":";
        }
        if (minutes > 9) {
            timeStr += minutes + ":";
        } else if (minutes > 0) {
            timeStr += "0" + minutes + ":";
        } else {
            timeStr += "00:";
        }
        if (seconds > 9) {
            timeStr += seconds;
        } else if (seconds > 0) {
            timeStr += "0" + seconds;
        } else {
            timeStr += "00";
        }
        return timeStr;
    }
}
