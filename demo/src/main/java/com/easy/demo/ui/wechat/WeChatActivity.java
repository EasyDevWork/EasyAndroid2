package com.easy.demo.ui.wechat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestWechatBinding;
import com.easy.framework.base.BaseActivity;
import com.easy.net.event.ActivityEvent;
import com.easy.wechat.base.Constants;
import com.easy.wechat.manager.WeChatManager;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

@ActivityInject
@Route(path = "/demo/WeChatActivity", name = "微信测试")
public class WeChatActivity extends BaseActivity<WeChatPresenter, TestWechatBinding> implements WeChatView<ActivityEvent> {
    private static final int THUMB_SIZE = 150;

    @Override
    public int getLayoutId() {
        return R.layout.test_wechat;
    }

    @Override
    public void initView() {
        WeChatManager.getInstance().init(this, Constants.APP_ID, Constants.APP_SECRET);
        viewBind.sendText.setOnClickListener(v -> WeChatManager.getInstance().sendTextMsg("测试发送文本信息"));

        viewBind.sendImg.setOnClickListener(v -> {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), com.easy.wechat.R.drawable.send_img);
            WeChatManager.getInstance().sendImageMsg(bmp);

//            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.png";
//            File file = new File(path);
//            if (!file.exists()) {
//                String tip = SendToWXActivity.this.getString(R.string.send_img_file_not_exist);
//                Toast.makeText(SendToWXActivity.this, tip + " path = " + path, Toast.LENGTH_LONG).show();
//                break;
//            }
//            WeChatManager.getInstance().sendImageMsg(file);
        });

        viewBind.sendMusic.setOnClickListener(v -> {
            Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), com.easy.wechat.R.drawable.send_music_thumb);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
            bmp.recycle();
            WeChatManager.getInstance().sendMusic(
                    "http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3",
                    "title",
                    "description", thumbBmp);
        });

        viewBind.sendVideo.setOnClickListener(v -> {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), com.easy.wechat.R.drawable.send_music_thumb);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
            bmp.recycle();
            WeChatManager.getInstance().sendVideo("http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3", "Video Title", "Video Description", thumbBmp);
        });

        viewBind.sendWebpage.setOnClickListener(v -> {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), com.easy.wechat.R.drawable.send_img);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
            bmp.recycle();
            WeChatManager.getInstance().sendWabPage("http://www.qq.com",
                    "WebPage Title", "WebPage Description", thumbBmp);
        });

        // get user info by token
        viewBind.getInfo.setOnClickListener(v -> {
            WeChatManager.getInstance().getUserInfo(weChatUser -> {
                        TextView tvResult = findViewById(R.id.tvResult);
                        tvResult.post(() -> tvResult.setText(weChatUser.toString()));
                    }
            );
        });
    }

    public void onRadioButtonClicked(View view) {
        if (!(view instanceof RadioButton)) {
            return;
        }
        boolean checked = ((RadioButton) view).isChecked();
        int id = view.getId();
        if (id == R.id.target_scene_session) {
            if (checked) {
                WeChatManager.getInstance().setTargetScene(SendMessageToWX.Req.WXSceneSession);
            }
        } else if (id == R.id.target_scene_timeline) {
            if (checked) {
                WeChatManager.getInstance().setTargetScene(SendMessageToWX.Req.WXSceneTimeline);
            }
        } else if (id == R.id.target_scene_favorite) {
            if (checked) {
                WeChatManager.getInstance().setTargetScene(SendMessageToWX.Req.WXSceneFavorite);
            }
        }
    }
}
