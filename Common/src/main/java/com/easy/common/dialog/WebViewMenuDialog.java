package com.easy.common.dialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.FileProvider;

import com.easy.common.R;
import com.easy.common.ui.web.activity.WebActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

public class WebViewMenuDialog extends AppCompatDialogFragment {
    Uri contentUri;

    public static WebViewMenuDialog newInstance(Bundle args) {
        WebViewMenuDialog fragment = new WebViewMenuDialog();
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
                    Activity activity = getActivity();
                    if (activity instanceof WebActivity) {
                        ((WebActivity) activity).cancelDialog();
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
            if (activity instanceof WebActivity) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                activity.startActivityForResult(Intent.createChooser(i, "Image Chooser"), WebActivity.FILE);
            }
            WebViewMenuDialog.this.dismiss();
        });
        view.findViewById(R.id.tvPhotograph).setOnClickListener(v -> {
            if (getActivity() != null) {
                RxPermissions permissions = new RxPermissions(getActivity());
                permissions.request(Manifest.permission.CAMERA).subscribe(granted -> {
                    Activity activity = getActivity();
                    if (activity instanceof WebActivity) {
                        File mTempFile = new File(WebActivity.PHOTO_PATH);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            contentUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", mTempFile);
                        } else {
                            contentUri = Uri.fromFile(mTempFile);
                        }

                        if (granted) {
                            //步骤四：调取系统拍照
                            Intent intent = new Intent();
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                            activity.startActivityForResult(intent, WebActivity.CAPTURE);
                        }
                    }
                    WebViewMenuDialog.this.dismiss();
                }, throwable -> WebViewMenuDialog.this.dismiss());
            }
        });

        view.findViewById(R.id.tvVideo).setOnClickListener(v -> {
            if (getActivity() != null) {
                RxPermissions permissions = new RxPermissions(getActivity());
                permissions.request(Manifest.permission.CAMERA).subscribe(granted -> {
                    Activity activity = getActivity();
                    if (activity instanceof WebActivity) {
                        File mTempFile = new File(WebActivity.VIDEO_PATH);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            contentUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", mTempFile);
                        } else {
                            contentUri = Uri.fromFile(mTempFile);
                        }
                        if (granted) {
                            //步骤四：调取系统拍照
                            Intent intent = new Intent();
                            intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                            activity.startActivityForResult(intent, WebActivity.VIDEO);
                        }
                    }
                    WebViewMenuDialog.this.dismiss();
                }, throwable -> WebViewMenuDialog.this.dismiss());
            }
        });
        return view;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Activity activity = getActivity();
        if (activity instanceof WebActivity) {
            ((WebActivity) getActivity()).cancelDialog();
        }
    }
}
