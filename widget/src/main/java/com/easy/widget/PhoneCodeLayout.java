package com.easy.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class PhoneCodeLayout extends RelativeLayout {
    private Context context;
    private TextView tv_code1;
    private TextView tv_code2;
    private TextView tv_code3;
    private TextView tv_code4;
    private TextView tv_code5;
    private TextView tv_code6;
    private View v1;
    private View v2;
    private View v3;
    private View v4;
    private View v5;
    private View v6;
    private EditText et_code;
    private List<String> codes = new ArrayList<>();
    private InputMethodManager imm;
    private boolean isPassword;


    public void setPassword(boolean password) {
        isPassword = password;
    }

    public PhoneCodeLayout(Context context) {
        super(context);
        this.context = context;
        loadView();
    }

    public PhoneCodeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        loadView();
    }

    private void loadView() {
        isPassword = false;
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = LayoutInflater.from(context).inflate(R.layout.phone_code_view, this);
        initView(view);
        initEvent();
    }

    private void initView(View view) {
        tv_code1 = view.findViewById(R.id.tv_code1);
        tv_code2 = view.findViewById(R.id.tv_code2);
        tv_code3 = view.findViewById(R.id.tv_code3);
        tv_code4 = view.findViewById(R.id.tv_code4);
        tv_code5 = view.findViewById(R.id.tv_code5);
        tv_code6 = view.findViewById(R.id.tv_code6);
        et_code = view.findViewById(R.id.et_code);
        v1 = view.findViewById(R.id.v1);
        v2 = view.findViewById(R.id.v2);
        v3 = view.findViewById(R.id.v3);
        v4 = view.findViewById(R.id.v4);
        v5 = view.findViewById(R.id.v5);
        v6 = view.findViewById(R.id.v6);
        et_code.postDelayed(new Runnable() {
            @Override
            public void run() {
                forceShow(et_code);
                et_code.setFocusable(true);
                et_code.setFocusableInTouchMode(true);
                et_code.requestFocus();
            }
        }, 100);

    }
    /**
     * 强制显示键盘
     *
     * @param view
     */
    public  void forceShow(View view) {
        if (view != null && view.getContext() != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    private void initEvent() {
        //验证码输入
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && editable.length() > 0) {
                    et_code.setText("");
                    if (codes.size() < 6) {
                        codes.add(editable.toString());
                        showCode(isPassword, codes);
                    }
                }
            }
        });
        // 监听验证码删除/确定按键
        et_code.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN && codes.size() > 0) {
                    codes.remove(codes.size() - 1);
                    showCode(isPassword, codes);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP && codes.size() > 0) {
                    if (onInputListener != null) {
                        forceHide(et_code);
                        onInputListener.onSuccess(getPhoneCode());
//                        PhoneCodeLayout.this.clean();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * 强制显示键盘
     *
     * @param view
     */
    public static void forceHide(View view) {
        if (view != null && view.getContext() != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && view.getWindowToken() != null && imm.isActive()) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } else {
                Log.w("KeyBoardUtils", "something error!");
            }
        }
    }
    /**
     * 显示输入的验证码
     */
    private void showCode(boolean isPassword, List<String> codes) {
        String code1 = "";
        String code2 = "";
        String code3 = "";
        String code4 = "";
        String code5 = "";
        String code6 = "";
        if (isPassword) {
            if (codes.size() >= 1) {
                code1 = "•";
            }
            if (codes.size() >= 2) {
                code2 = "•";
            }
            if (codes.size() >= 3) {
                code3 = "•";
            }
            if (codes.size() >= 4) {
                code4 = "•";
            }
            if (codes.size() >= 5) {
                code5 = "•";
            }
            if (codes.size() >= 6) {
                code6 = "•";
            }
        } else {
            if (codes.size() >= 1) {
                code1 = codes.get(0);
            }
            if (codes.size() >= 2) {
                code2 = codes.get(1);
            }
            if (codes.size() >= 3) {
                code3 = codes.get(2);
            }
            if (codes.size() >= 4) {
                code4 = codes.get(3);
            }
            if (codes.size() >= 5) {
                code5 = codes.get(4);
            }
            if (codes.size() >= 6) {
                code6 = codes.get(5);
            }
        }
        tv_code1.setText(code1);
        tv_code2.setText(code2);
        tv_code3.setText(code3);
        tv_code4.setText(code4);
        tv_code5.setText(code5);
        tv_code6.setText(code6);

        setColor();//设置高亮颜色
        callBack();//回调
    }

    public void clean() {
        et_code.setText("");
        codes.clear();
        showCode(isPassword, codes);
        showSoftInput();
    }

    /**
     * 设置高亮颜色
     */
    private void setColor() {
        int color_default = context.getResources().getColor(R.color.phone_code_divider_color);
        int color_focus = context.getResources().getColor(R.color.phone_code_divider_focus_color);
        v1.setBackgroundColor(color_default);
        v2.setBackgroundColor(color_default);
        v3.setBackgroundColor(color_default);
        v4.setBackgroundColor(color_default);
        v5.setBackgroundColor(color_default);
        v6.setBackgroundColor(color_default);
        if (codes.size() == 0) {
            v1.setBackgroundColor(color_focus);
        }
        if (codes.size() == 1) {
            v2.setBackgroundColor(color_focus);
        }
        if (codes.size() == 2) {
            v3.setBackgroundColor(color_focus);
        }
        if (codes.size() == 3) {
            v4.setBackgroundColor(color_focus);
        }
        if (codes.size() == 4) {
            v5.setBackgroundColor(color_focus);
        }
        if (codes.size() >= 5) {
            v6.setBackgroundColor(color_focus);
        }
    }

    /**
     * 回调
     */
    private void callBack() {
        if (onInputListener == null) {
            return;
        }
        if (codes.size() == 6) {
            forceHide(et_code);
            onInputListener.onSuccess(getPhoneCode());
        } else {
            onInputListener.onInput();
        }
    }

    public void setText(String copy, boolean isPwd) {
        if (copy == null || copy.length() != 6) {
            return;
        }
        List<String> cs = new ArrayList<>();
        char[] chars = copy.toCharArray();
        for (int i = 0; i < copy.length(); ++i) {
            cs.add(chars[i] + "");
        }
        showCode(isPwd, cs);

    }

    //定义回调
    public interface OnInputListener {
        void onSuccess(String code);

        void onInput();
    }

    private OnInputListener onInputListener;

    public void setOnInputListener(OnInputListener onInputListener) {
        this.onInputListener = onInputListener;
    }

    /**
     * 显示键盘
     */
    public void showSoftInput() {
        //显示软键盘
        if (imm != null && et_code != null) {
            et_code.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imm.showSoftInput(et_code, 0);
                }
            }, 200);
        }
    }

    /**
     * 显示键盘
     */
    public void hideSoftInput() {
        //显示软键盘
        if (imm != null && et_code != null) {
            et_code.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imm.hideSoftInputFromWindow(et_code.getWindowToken(), 0);
                }
            }, 200);
        }
    }

    /**
     * 获得手机号验证码
     *
     * @return 验证码
     */
    public String getPhoneCode() {
        StringBuilder sb = new StringBuilder();
        for (String code : codes) {
            sb.append(code);
        }
        return sb.toString();
    }
}
