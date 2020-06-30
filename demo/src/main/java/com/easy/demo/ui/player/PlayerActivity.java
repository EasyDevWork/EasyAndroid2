package com.easy.demo.ui.player;

import android.content.res.Configuration;
import android.util.Log;
import android.view.KeyEvent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.aliyun.player.IPlayer;
import com.easy.aliplayer.base.ThemeEnum;
import com.easy.aliplayer.listener.OnChangeQualityListener;
import com.easy.aliplayer.listener.OnOrientationListener;
import com.easy.aliplayer.listener.OnScreenBrightnessListener;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestPlayerBinding;
import com.easy.framework.base.BaseActivity;
import com.easy.framework.manager.screen.ScreenOrientationLiveData;
import com.easy.framework.watch.NetStatusWatch;
import com.easy.utils.BrightnessUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

@ActivityInject
@Route(path = "/demo/PlayerActivity", name = "播放器页面")
public class PlayerActivity extends BaseActivity<PlayerPresenter, TestPlayerBinding> implements PlayerView {
    private final static String TAG = "PlayerActivity";
    OnOrientationListener onOrientationListener;

    @Override
    public int getLayoutId() {
        return R.layout.test_player;
    }

    @Override
    public void initView() {
        initPlayerView();
        viewBind.playerView.setCoverResource(R.drawable.testimg);//可选
        viewBind.playerView.setPlaySource("http://player.alicdn.com/video/aliyunmedia.mp4");
        viewBind.playerView.setVideoScalingMode(IPlayer.ScaleMode.SCALE_TO_FILL);//可选
//        viewBind.playerView.setLockPortraitMode(LockPortrait.FIX_NONE);//可选

        onOrientationListener = viewBind.playerView.getOrientationWatchdog();
        ScreenOrientationLiveData screenOrientationLiveData = new ScreenOrientationLiveData(this);
        screenOrientationLiveData.observe(this, screenOrientation -> {
            Log.d("ScreenOrientation", screenOrientation.toString());
            if (onOrientationListener != null && screenOrientation.isChange) {
                if (screenOrientation.orientation == ScreenOrientationLiveData.Orientation.PORT_REVERSE
                        || screenOrientation.orientation == ScreenOrientationLiveData.Orientation.PORT_FORWARD) {
                    onOrientationListener.changedToPortrait(true);
                } else if (screenOrientation.orientation == ScreenOrientationLiveData.Orientation.LAND_REVERSE) {
                    onOrientationListener.changedToLandReverseScape(true);
                } else {
                    onOrientationListener.changedToLandScape(true);
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updatePlayerViewMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePlayerViewMode();
        viewBind.playerView.setAutoPlay(true);
        viewBind.playerView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewBind.playerView.setAutoPlay(false);
        viewBind.playerView.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean handler = viewBind.playerView.onKeyDown(keyCode, event);
        if (!handler) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //解决某些手机上锁屏之后会出现标题栏的问题。
        updatePlayerViewMode();
    }

    @Override
    public void onDestroy() {
        viewBind.playerView.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event != null && event.getKeyCode() == 67) {
            //删除按键监听,部分手机在EditText没有内容时,点击删除按钮会隐藏软键盘
            return false;
        }
        return super.dispatchKeyEvent(event);
    }


    private void updatePlayerViewMode() {
        viewBind.playerView.updatePlayerViewMode(this);
    }

    private void initPlayerView() {
        //保持屏幕敞亮
        viewBind.playerView.setKeepScreenOn(true);
        viewBind.playerView.setTheme(ThemeEnum.Blue);
        viewBind.playerView.setCirclePlay(true);
        viewBind.playerView.setAutoPlay(true);
        viewBind.playerView.setOnPreparedListener(() -> {
            Log.d(TAG, "playerPrepared==>播放器准备成功");
        });
        viewBind.playerView.setNetConnectedListener(new NetStatusWatch.NetConnectedListener() {
            @Override
            public void onReNetConnected(boolean isReconnect) {
                Log.d(TAG, "reNetConnected==>网络恢复 isReconnect:" + isReconnect);
            }

            @Override
            public void onNetUnConnected() {
                Log.d(TAG, "netUnConnected==>网络异常");
            }
        });
        viewBind.playerView.setOnCompletionListener(() -> {
            Log.d(TAG, "playerCompletion==>播放器完成");
        });
        viewBind.playerView.setOnFirstFrameStartListener(() -> {
            Log.d(TAG, "playerFirstFrameStart==>第一帧播放完成 ");
        });
        viewBind.playerView.setOnChangeQualityListener(new OnChangeQualityListener() {
            @Override
            public void onChangeQualitySuccess(String quality) {
                Log.d(TAG, "playerChangeQualitySuccess==>修改播放切换清晰度成功：" + quality);
            }

            @Override
            public void onChangeQualityFail(int code, String msg) {
                Log.d(TAG, "playerChangeQualityFail==>修改播放切换清晰度失败code=" + code + " msg=" + msg);
            }
        });

        viewBind.playerView.setOnStoppedListener(() -> {
            Log.d(TAG, "playerStop==>播放器停止");
        });
        viewBind.playerView.setmOnPlayerViewClickListener((screenMode, viewType) -> {
            Log.d(TAG, "playerClick==>点击播放按钮 屏幕模式screenMode=" + screenMode + " viewType=" + viewType);
        });
        viewBind.playerView.setOnTimeExpiredErrorListener(() -> {
            Log.d(TAG, "playerTimeExpiredError==>鉴权过期 ");
        });
        viewBind.playerView.setOnPlayStateBtnClickListener(playerState -> {
            if (playerState == IPlayer.started) {//暂停
                Log.d(TAG, "playStateSwitch==>播放状态切换 playerState=" + playerState);
            } else if (playerState == IPlayer.paused) {//开始
                Log.d(TAG, "playStateSwitch==>播放状态切换 playerState=" + playerState);
            }
        });
        viewBind.playerView.setOnSeekCompleteListener(() -> {
            Log.d(TAG, "seekComplete==>拉进度条完成");
        });
        viewBind.playerView.setOnSeekStartListener(position -> {
            Log.d(TAG, "seekComplete==>拉进度条开始 ：position=" + position);
        });
        viewBind.playerView.setOnErrorListener(errorInfo -> {
            Log.d(TAG, "playerError==>播放异常 ：errorInfo=" + errorInfo.toString());
        });
        viewBind.playerView.setOnScreenBrightnessListener(new OnScreenBrightnessListener() {
            @Override
            public void setScreenBrightness(int brightness) {
                BrightnessUtils.changeAppBrightness(PlayerActivity.this, brightness);
            }

            @Override
            public int getScreenBrightness() {
                return BrightnessUtils.getSystemBrightness(PlayerActivity.this);
            }
        });
        viewBind.playerView.setSeiDataListener((i, bytes) -> {
            String seiMessage = new String(bytes);
            String log = new SimpleDateFormat("HH:mm:ss.SS").format(new Date()) + "SEI:type:" + i + ",content:" + seiMessage + "\n";
            Log.d(TAG, "playerSeiData==>SeiData ：log=" + log);
        });
        viewBind.playerView.enableNativeLog();
    }
}
