package com.easy.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.easy.common.R;


public class AppUpdateDialog extends Dialog {
    View.OnClickListener listener;
    TextView tvContent, tvCommit, tvTitle, tvProgress;
    LinearLayout llProgress;
    ProgressBar progressBar;
    boolean isDownloaded;

    public AppUpdateDialog(@NonNull Context context) {
        super(context);
        initDialog();
    }

    private void initDialog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.app_update_dialog, null);
        setContentView(view);
        initView(view);
    }

    private void initView(View view) {
        tvContent = view.findViewById(R.id.tvContent);
        tvCommit = view.findViewById(R.id.tvCommit);
        tvTitle = view.findViewById(R.id.tvTitle);
        llProgress = view.findViewById(R.id.llProgress);
        progressBar = view.findViewById(R.id.progressBar);
        tvProgress = view.findViewById(R.id.tvProgress);
        View imageClose = view.findViewById(R.id.imageClose);
        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvContent.setMovementMethod(new ScrollingMovementMethod());
    }

    public void setData(boolean downloaded, String title, String content, View.OnClickListener onClickListener) {
        listener = onClickListener;
        isDownloaded = downloaded;
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        if (!TextUtils.isEmpty(content)) {
            content = content.replace(";", ";\n");
        }
        tvContent.setText(content);
        if (isDownloaded) {
            tvCommit.setText(getContext().getResources().getString(R.string.install_app));
        }
        setCanceledOnTouchOutside(false);
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDownloaded) {
                    tvCommit.setVisibility(View.GONE);
                    llProgress.setVisibility(View.VISIBLE);
                    progressBar.setProgress(0);
                    tvProgress.setText("0%");
                }
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });
    }

    @Override
    public void show() {
        super.show();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (display.getWidth()); //设置宽度
        getWindow().setAttributes(lp);

        Window dialogWindow = getWindow();
        ColorDrawable dw = new ColorDrawable(0x0000ff00);
        dialogWindow.setBackgroundDrawable(dw);
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setWindowAnimations(R.style.dialogBottomAnimation);
    }

    public void setProgress(int aLong) {
        progressBar.setProgress(aLong);
        tvProgress.setText(aLong + "%");
    }
}
