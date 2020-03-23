package com.easy.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDialogFragment;

public class WebViewMenuDialog extends AppCompatDialogFragment {
    Callback callback;

    public interface Callback {

        void cancelDialog();

        void choosePhotograph();//选择拍照

        void chooseVideo();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public static WebViewMenuDialog newInstance(Bundle args, Callback callback) {
        WebViewMenuDialog fragment = new WebViewMenuDialog();
        fragment.setCallback(callback);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setOnKeyListener((dialog2, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    WebViewMenuDialog.this.dismiss();
                    if (callback != null) {
                        callback.cancelDialog();
                    }
                    return true;
                }
                return false;
            });
        }

        View view = inflater.inflate(R.layout.web_view_menu, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getBoolean("isFeedBack", false)) {
                view.findViewById(R.id.tvVideo).setVisibility(View.GONE);
                view.findViewById(R.id.dvVideo).setVisibility(View.GONE);
            }
        }

        view.findViewById(R.id.tvAlbum).setOnClickListener(v -> {
            Activity activity = getActivity();
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            activity.startActivityForResult(Intent.createChooser(i, "Image Chooser"), 10000);
            WebViewMenuDialog.this.dismiss();
        });
        view.findViewById(R.id.tvPhotograph).setOnClickListener(v -> {
            if (callback != null) {
                callback.choosePhotograph();
            }
            WebViewMenuDialog.this.dismiss();
        });

        view.findViewById(R.id.tvVideo).setOnClickListener(v -> {
            if (callback != null) {
                callback.chooseVideo();
            }
            WebViewMenuDialog.this.dismiss();
        });
        return view;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (callback != null) {
            callback.cancelDialog();
        }
    }
}
