package com.easy.utils;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewUtils {
    //只能输入一位小数点
    public static void inputSinglePoint(EditText editText, CharSequence s) {
        if (!EmptyUtils.isEmpty(editText.getText().toString())) {
            if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    editText.setText(s.subSequence(0, 1));
                    editText.setSelection(1);
                    return;
                }
            }
            if (isPositiveInteger(s.toString()) || isPositiveDecimal(s.toString())) {
               /* if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }*/
            } else {
                editText.setText(s.toString().substring(0, s.length() - 1));
                editText.setSelection(editText.getText().length());

            }
        } else {

        }
    }

    private static boolean isMatch(String regex, String orginal) {
        if (orginal == null || orginal.trim().equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(orginal);
        return isNum.matches();
    }

    public static boolean isPositiveInteger(String orginal) {
        return isMatch("^\\+{0,1}[1-9]\\d*", orginal);
    }

    public static boolean isPositiveDecimal(String orginal) {
        return isMatch(
                "^\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*|^[0-9]+([.]{1}[0-9]+){0,1}$",
                orginal);
    }
}
