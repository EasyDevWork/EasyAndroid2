package com.easy.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


public class ClipBoardUtils {

    public static void copyToClipBoard(Context context, String key, String value) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(key, value);
        clipboardManager.setPrimaryClip(clipData);
    }

    public static void copyToClipBoard(Context context, String value) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("default", value);
        clipboardManager.setPrimaryClip(clipData);
    }

    public static void copyToClipBoardAndTip(Context context, String value) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("default", value);
        clipboardManager.setPrimaryClip(clipData);
    }

    public static void cleanPrimaryClipboard(Context context) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(null, "");
        clipboardManager.setPrimaryClip(clipData);
    }

    public static String paste(Context context) {
        try {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData abc = clipboardManager.getPrimaryClip();
            ClipData.Item item = abc.getItemAt(0);
            String text = item.getText().toString();
            if (!TextUtils.isEmpty(text)) {
                return text;
            } else {
                return "";
            }
        } catch (Exception e) {
            Log.e("paste","paste");
            return "";
        }
    }
}
