package com.easy.aliplayer.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.aliyun.player.AliPlayer;
import com.aliyun.player.AliPlayerFactory;
import com.aliyun.player.IPlayer;
import com.aliyun.player.bean.ErrorInfo;
import com.aliyun.player.bean.InfoBean;
import com.aliyun.player.bean.InfoCode;
import com.aliyun.player.nativeclass.MediaInfo;
import com.aliyun.player.nativeclass.PlayerConfig;
import com.aliyun.player.nativeclass.Thumbnail;
import com.aliyun.player.nativeclass.TrackInfo;
import com.aliyun.player.source.UrlSource;
import com.aliyun.player.source.VidAuth;
import com.aliyun.player.source.VidSts;
import com.aliyun.thumbnail.ThumbnailBitmapInfo;
import com.aliyun.thumbnail.ThumbnailHelper;
import com.aliyun.utils.VcPlayerLog;
import com.easy.aliplayer.R;
import com.easy.aliplayer.base.LockPortrait;
import com.easy.aliplayer.base.PlayViewType;
import com.easy.aliplayer.base.ScreenMode;
import com.easy.aliplayer.base.SpeedEnum;
import com.easy.aliplayer.base.ThemeEnum;
import com.easy.aliplayer.dialog.ShowMoreDialog;
import com.easy.aliplayer.handle.PlayerLoadEndHandler;
import com.easy.aliplayer.listener.OnAutoPlayListener;
import com.easy.aliplayer.listener.OnChangeQualityListener;
import com.easy.aliplayer.listener.OnPlayerViewClickListener;
import com.easy.aliplayer.listener.OnScreenBrightnessListener;
import com.easy.aliplayer.listener.OnStoppedListener;
import com.easy.aliplayer.listener.OnTimeExpiredErrorListener;
import com.easy.aliplayer.quality.QualityView;
import com.easy.aliplayer.interfaces.ITheme;
import com.easy.aliplayer.view.gesture.GestureDialogManager;
import com.easy.aliplayer.view.gesture.GestureView;
import com.easy.aliplayer.view.gesturedialog.BrightnessDialog;
import com.easy.aliplayer.view.gesturedialog.SeekDialog;
import com.easy.aliplayer.view.gesturedialog.VolumeDialog;
import com.easy.aliplayer.view.function.GuideView;
import com.easy.aliplayer.interfaces.ViewAction;
import com.easy.aliplayer.view.function.ShowMoreModel;
import com.easy.aliplayer.view.function.ShowMoreView;
import com.easy.aliplayer.view.function.SpeedView;
import com.easy.aliplayer.view.function.ThumbnailView;
import com.easy.aliplayer.view.tipsview.TipsView;
import com.easy.framework.utils.DateUtils;
import com.easy.framework.utils.DeviceUtils;
import com.easy.framework.utils.DimensUtils;
import com.easy.framework.utils.NetUtils;
import com.easy.framework.utils.ToastUtils;
import com.easy.framework.utils.VolumeUtils;
import com.easy.framework.watch.NetStatusWatch;
import com.easy.framework.watch.ScreenOrientationWatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UI播放器的主要实现类。 通过ITheme控制各个界面的主题色。 通过各种view的组合实现UI的界面。这些view包括： 用户手势操作的{@link GestureView} 控制播放，显示信息的{@link
 * ControlView} 显示清晰度列表的{@link QualityView} 倍速选择界面{@link SpeedView} 用户使用引导页面{@link GuideView} 用户提示页面{@link TipsView}
 * 以及封面等。 view 的初始化是在{@link #initVideoView}方法中实现的。 然后是对各个view添加监听方法，处理对应的操作，从而实现与播放器的共同操作
 * 播放支持:vid, vidSts, akId, akSecre, scuToken
 */

public class AliyunVodPlayerView extends RelativeLayout implements ITheme {
    /**
     * 精准seek开启判断逻辑：当视频时长小于5分钟的时候。
     */
    private static final int ACCURATE = 5 * 60 * 1000;
    private static final String TAG = AliyunVodPlayerView.class.getSimpleName();

    //视频画面
    private SurfaceView mSurfaceView;
    //播放器
    private AliPlayer mAliyunVodPlayer;
    //手势操作view
    private GestureView mGestureView;
    //皮肤view
    private ControlView mControlView;
    //清晰度view
    private QualityView mQualityView;
    //倍速选择view
    private SpeedView mSpeedView;
    //引导页view
    private GuideView mGuideView;
    //封面view
    private ImageView mCoverView;
    //缩略图View
    private ThumbnailView mThumbnailView;
    //Tips view
    private TipsView mTipsView;

    //对外的各种事件监听
    //网络状态监听
    private NetStatusWatch mNetWatchdog;
    //屏幕方向监听
    private ScreenOrientationWatch mOrientationWatchDog;
    //锁定竖屏
    private LockPortrait lockPortrait = LockPortrait.FIX_NONE;
    private IPlayer.OnInfoListener mOutInfoListener = null;
    private IPlayer.OnErrorListener mOutErrorListener = null;
    private OnScreenBrightnessListener mOutScreenBrightnessListener = null;
    private OnAutoPlayListener mOutAutoPlayListener = null;
    private IPlayer.OnPreparedListener mOutPreparedListener = null;
    private IPlayer.OnCompletionListener mOutCompletionListener = null;
    private IPlayer.OnSeekCompleteListener mOuterSeekCompleteListener = null;
    private OnChangeQualityListener mOutChangeQualityListener = null;
    private IPlayer.OnRenderingStartListener mOutFirstFrameStartListener = null;
    private OnTimeExpiredErrorListener mOutTimeExpiredErrorListener = null;
    //对外view点击事件监听
    private OnPlayerViewClickListener mOnPlayerViewClickListener = null;
    // 连网断网监听
    private NetStatusWatch.NetConnectedListener mNetConnectedListener = null;
    //播放按钮点击监听
    private OnPlayStateBtnClickListener onPlayStateBtnClickListener;
    //停止按钮监听
    private OnStoppedListener mOnStoppedListener;
    //对外SEI消息通知
    private IPlayer.OnSeiDataListener mOutSeiDataListener = null;

    //目前支持的几种播放方式
    private VidAuth mVidAuth;
    private UrlSource mUrlSource;
    private VidSts mVidSts;

    //缩略图帮助类
    private ThumbnailHelper mThumbnailHelper;
    //判断VodePlayer 是否加载完成
    private Map<MediaInfo, Boolean> hasLoadEnd = new HashMap<>();
    //手势对话框控制
    private GestureDialogManager mGestureDialogManager;
    //当前屏幕模式
    private ScreenMode mCurrentScreenMode = ScreenMode.Small;
    //是否锁定全屏
    private boolean mIsFullScreenLocked = false;

    //是不是在seek中
    private boolean inSeek = false;
    //播放是否完成
    private boolean isCompleted = false;
    //媒体信息
    private MediaInfo mAliyunMediaInfo;
    //解决bug,进入播放界面快速切换到其他界面,播放器仍然播放视频问题
    private PlayerLoadEndHandler vodPlayerLoadEndHandler = new PlayerLoadEndHandler(this);
    //原视频的buffered
    private long mVideoBufferedPosition = 0;
    //原视频的currentPosition
    private long mCurrentPosition = 0;
    //当前播放器的状态 默认为idle状态
    private int mPlayerState = IPlayer.idle;
    //原视频时长
    private long mSourceDuration;
    //获取缩略图是否成功
    private boolean mThumbnailPrepareSuccess = false;
    private float currentSpeed = 1.0f;
    private int currentBrightness;
    private float currentVolume = 50;//0-100 音量

    public AliyunVodPlayerView(Context context) {
        super(context);
        initVideoView();
    }

    public AliyunVodPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVideoView();
    }

    public AliyunVodPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVideoView();
    }

    /**
     * 初始化view
     */
    private void initVideoView() {
        initData();
        //初始化播放用的surfaceView
        initSurfaceView();
        //初始化播放器
        initAliPlayer();
        //初始化封面
        initCoverView();
        //初始化手势view
        initGestureView();
        //初始化控制栏
        initControlView();
        //初始化清晰度view
        initQualityView();
        //初始化缩略图
        initThumbnailView();
        //初始化倍速view
        initSpeedView();
        //初始化指引view
        initGuideView();
        //初始化提示view
        initTipsView();
        //初始化网络监听器
        initNetWatchdog();
        //初始化屏幕方向监听
        initOrientationWatchdog();
        //初始化手势对话框控制
        initGestureDialogManager();
        //默认为蓝色主题
        setTheme(ThemeEnum.Blue);
        //先隐藏手势和控制栏，防止在没有prepare的时候做操作。
        hideGestureAndControlViews();
    }

    /**
     * 初始化初始值
     */
    private void initData() {
        //获取当前屏幕亮度
        if (mOutScreenBrightnessListener != null) {
            currentBrightness = mOutScreenBrightnessListener.getScreenBrightness();
        }
        currentVolume = VolumeUtils.getVolumeSize(getContext());
    }

    /**
     * 初始化播放器显示view
     */
    private void initSurfaceView() {
        mSurfaceView = new SurfaceView(getContext().getApplicationContext());
        addSubView(mSurfaceView);

        SurfaceHolder holder = mSurfaceView.getHolder();
        //增加surfaceView的监听
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                Log.d(TAG, "surfaceCreated");
                if (mAliyunVodPlayer != null) {
                    mAliyunVodPlayer.setDisplay(surfaceHolder);
                    mAliyunVodPlayer.redraw();  //防止黑屏
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
                Log.d(TAG, "surfaceChanged width = " + width + " , height = " + height);
                if (mAliyunVodPlayer != null) {
                    mAliyunVodPlayer.redraw();
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                Log.d(TAG, " surfaceDestroyed");
                if (mAliyunVodPlayer != null) {
                    mAliyunVodPlayer.setDisplay(null);
                }
            }
        });
    }

    /**
     * 初始化播放器
     */
    private void initAliPlayer() {
        mAliyunVodPlayer = AliPlayerFactory.createAliPlayer(getContext().getApplicationContext());
        mAliyunVodPlayer.enableLog(false);
        //设置准备回调--用于广告视频播放器准备对外接口监听
        mAliyunVodPlayer.setOnPreparedListener(() -> sourceVideoPlayerPrepared());
        //播放器出错监听
        mAliyunVodPlayer.setOnErrorListener(errorInfo -> sourceVideoPlayerError(errorInfo));
        //播放器加载回调-播放器加载状态监听
        mAliyunVodPlayer.setOnLoadingStatusListener(new IPlayer.OnLoadingStatusListener() {
            @Override
            public void onLoadingBegin() {
                sourceVideoPlayerLoadingBegin();
            }

            @Override
            public void onLoadingProgress(int percent, float v) {
                sourceVideoPlayerLoadingProgress(percent);
            }

            @Override
            public void onLoadingEnd() {
                sourceVideoPlayerLoadingEnd();
            }
        });
        mAliyunVodPlayer.setOnSnapShotListener((bitmap, i, i1) -> ToastUtils.showShort("截图完成，自行开发"));
        //播放器状态
        mAliyunVodPlayer.setOnStateChangedListener(change -> sourceVideoPlayerStateChanged(change));
        //播放结束
        mAliyunVodPlayer.setOnCompletionListener(() -> sourceVideoPlayerCompletion());
        //播放信息监听
        mAliyunVodPlayer.setOnInfoListener(infoBean -> sourceVideoPlayerInfo(infoBean));
        //第一帧显示
        mAliyunVodPlayer.setOnRenderingStartListener(() -> sourceVideoPlayerOnVideoRenderingStart());
        //trackChange监听
        mAliyunVodPlayer.setOnTrackChangedListener(new IPlayer.OnTrackChangedListener() {
            @Override
            public void onChangedSuccess(TrackInfo trackInfo) {
                sourceVideoPlayerTrackInfoChangedSuccess(trackInfo);
            }

            @Override
            public void onChangedFail(TrackInfo trackInfo, ErrorInfo errorInfo) {
                sourceVideoPlayerTrackInfoChangedFail(trackInfo, errorInfo);
            }
        });
        //seek结束事件
        mAliyunVodPlayer.setOnSeekCompleteListener(() -> sourceVideoPlayerSeekComplete());
        mAliyunVodPlayer.setOnSeiDataListener((var1, var2) -> sourceVideoPlayerSeiData(var1, var2));
        mAliyunVodPlayer.setDisplay(mSurfaceView.getHolder());
    }

    /**
     * 初始化封面
     */
    private void initCoverView() {
        mCoverView = new ImageView(getContext());
        //这个是为了给自动化测试用的id
        mCoverView.setId(R.id.custom_id_min);
        addSubView(mCoverView);
    }

    /**
     * 初始化控制栏view
     */
    private void initControlView() {
        mControlView = new ControlView(getContext());
        addSubView(mControlView);
        //设置播放按钮点击
        mControlView.setOnPlayStateClickListener(() -> switchPlayerState());
        //设置进度条的seek监听
        mControlView.setOnSeekListener(new ControlView.OnSeekListener() {
            @Override
            public void onSeekEnd(int position) {
                if (mControlView != null) {
                    mControlView.setVideoPosition(position);
                }
                if (isCompleted) {//播放完成了，不能seek了
                    inSeek = false;
                } else { //拖动结束后，开始seek
                    seekTo(position);
                    if (onSeekStartListener != null) {
                        onSeekStartListener.onSeekStart(position);
                    }
                    hideThumbnailView();
                }
            }

            @Override
            public void onSeekStart(int position) { //拖动开始
                inSeek = true;
                if (mThumbnailPrepareSuccess) {
                    showThumbnailView();
                }
            }

            @Override
            public void onProgressChanged(int progress) {
                requestBitmapByPosition(progress);
            }
        });
        //菜单按钮点击
        mControlView.setOnMenuClickListener(() -> {
            //点击之后显示倍速界面
            //根据屏幕模式，显示倍速界面
            mSpeedView.show(mCurrentScreenMode);
        });
        mControlView.setOnDownloadClickListener(() -> {
            if (mOnPlayerViewClickListener != null) {
                mOnPlayerViewClickListener.onClick(mCurrentScreenMode, PlayViewType.Download);
            }
        });
        //清晰度按钮点击
        mControlView.setOnQualityBtnClickListener(new ControlView.OnQualityBtnClickListener() {
            @Override
            public void onQualityBtnClick(View v, List<TrackInfo> qualities, String currentQuality) {
                //显示清晰度列表
                mQualityView.setQuality(qualities, currentQuality);
                mQualityView.showAtTop(v);
            }

            @Override
            public void onHideQualityView() {
                mQualityView.hide();
            }
        });
        //点击锁屏的按钮
        mControlView.setOnScreenLockClickListener(() -> lockScreen(!mIsFullScreenLocked));
        //点击全屏/小屏按钮
        mControlView.setOnScreenModeClickListener(() -> {
            ScreenMode targetMode;
            if (mCurrentScreenMode == ScreenMode.Small) {
                targetMode = ScreenMode.Full;
            } else {
                targetMode = ScreenMode.Small;
            }

            changeScreenMode(targetMode, false);
            if (mCurrentScreenMode == ScreenMode.Full) {
                mControlView.showMoreButton();
            } else if (mCurrentScreenMode == ScreenMode.Small) {
                mControlView.hideMoreButton();
            }
        });
        //点击了标题栏的返回按钮
        mControlView.setOnBackClickListener(() -> {
            if (mCurrentScreenMode == ScreenMode.Full) {
                //设置为小屏状态
                changeScreenMode(ScreenMode.Small, false);
            } else if (mCurrentScreenMode == ScreenMode.Small) {
                //小屏状态下，就结束活动
                Context context = getContext();
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }

            if (mCurrentScreenMode == ScreenMode.Small) {
                mControlView.hideMoreButton();
            }
        });

        // 横屏下显示更多
        mControlView.setOnShowMoreClickListener(() -> {
            playerShowMore();
        });

        // 截屏
        mControlView.setOnScreenShotClickListener(() -> {
            if (!mIsFullScreenLocked) {
                snapShot();
            }
        });

        // 录制
        mControlView.setOnScreenRecoderClickListener(() -> {
            if (!mIsFullScreenLocked) {
                ToastUtils.showShort("功能正在开发中, 敬请期待....");
            }
        });
        //弹幕
        mControlView.setOnDanmuClickListener(v -> {
            if (!mIsFullScreenLocked) {
                ToastUtils.showShort("功能正在开发中, 敬请期待....");
            }
        });
    }

    /**
     * 初始化清晰度列表
     */
    private void initQualityView() {
        mQualityView = new QualityView(getContext());
        addSubView(mQualityView);
        //清晰度点击事件
        mQualityView.setOnQualityClickListener(qualityTrackInfo -> {
            mAliyunVodPlayer.selectTrack(qualityTrackInfo.getIndex());
        });
    }

    /**
     * 初始化缩略图
     */
    private void initThumbnailView() {
        mThumbnailView = new ThumbnailView(getContext());
        mThumbnailView.setVisibility(View.GONE);
        addSubViewByCenter(mThumbnailView);
    }

    /**
     * 初始化倍速view
     */
    private void initSpeedView() {
        mSpeedView = new SpeedView(getContext());
        addSubView(mSpeedView);

        //倍速点击事件
        mSpeedView.setOnSpeedClickListener(new SpeedView.OnSpeedClickListener() {
            @Override
            public void onSpeedClick(SpeedView.SpeedValue value) {
                float speed = 1.0f;
                if (value == SpeedView.SpeedValue.Normal) {
                    speed = 1.0f;
                } else if (value == SpeedView.SpeedValue.OneQuartern) {
                    speed = 1.25f;
                } else if (value == SpeedView.SpeedValue.OneHalf) {
                    speed = 1.5f;
                } else if (value == SpeedView.SpeedValue.Twice) {
                    speed = 2.0f;
                }

                //改变倍速
                if (mAliyunVodPlayer != null) {
                    mAliyunVodPlayer.setSpeed(speed);
                }

                mSpeedView.setSpeed(value);
            }

            @Override
            public void onHide() {
                //当倍速界面隐藏之后，显示菜单按钮
            }
        });
    }

    /**
     * 初始化引导view
     */
    private void initGuideView() {
        mGuideView = new GuideView(getContext());
        addSubView(mGuideView);
    }

    /**
     * 初始化提示view
     */
    private void initTipsView() {
        mTipsView = new TipsView(getContext());
        //设置tip中的点击监听事件
        mTipsView.setOnTipClickListener(new TipsView.OnTipClickListener() {
            @Override
            public void onContinuePlay() {
                Log.d(TAG, "playerState = " + mPlayerState);
                //继续播放。如果没有prepare或者stop了，需要重新prepare
                mTipsView.hideAll();
                if (mPlayerState == IPlayer.paused || mPlayerState == IPlayer.prepared) {
                    start();
                } else {
                    if (mVidAuth != null) {
                        prepareAuth(mVidAuth);
                    } else if (mVidSts != null) {
                        prepareVidsts(mVidSts);
                    } else if (mUrlSource != null) {
                        prepareLocalSource(mUrlSource);
                    }
                }
            }

            @Override
            public void onStopPlay() {
                // 结束播放
                mTipsView.hideAll();
                stop();

                Context context = getContext();
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }

            @Override
            public void onRetryPlay() {
                //重试
                reTry();
            }

            @Override
            public void onReplay() {
                //重播
                rePlay();
            }

            @Override
            public void onRefreshSts() {
                if (mOutTimeExpiredErrorListener != null) {
                    mOutTimeExpiredErrorListener.onTimeExpiredError();
                }
            }
        });
        addSubView(mTipsView);
    }

    /**
     * 初始化网络监听
     */
    private void initNetWatchdog() {
        Context context = getContext();
        mNetWatchdog = new NetStatusWatch(context);
        mNetWatchdog.setNetChangeListener(new NetStatusWatch.NetChangeListener() {
            @Override
            public void onWifiTo4G() {
                wifiTo4G();
            }

            @Override
            public void on4GToWifi() {
                o4GToWifi();
            }

            @Override
            public void onNetDisconnected() {
                Log.d(TAG, "onNetDisconnected");
                //网络断开。
                // NOTE： 由于安卓这块网络切换的时候，有时候也会先报断开。所以这个回调是不准确的。
            }
        });
        mNetWatchdog.setNetConnectedListener(new NetStatusWatch.NetConnectedListener() {
            @Override
            public void onReNetConnected(boolean isReconnect) {
                if (mNetConnectedListener != null) {
                    mNetConnectedListener.onReNetConnected(isReconnect);
                }
            }

            @Override
            public void onNetUnConnected() {
                if (mNetConnectedListener != null) {
                    mNetConnectedListener.onNetUnConnected();
                }
            }
        });
    }

    /**
     * 初始化屏幕方向旋转。用来监听屏幕方向。结果通过OrientationListener回调出去。
     */
    private void initOrientationWatchdog() {
        mOrientationWatchDog = new ScreenOrientationWatch(getContext());
        mOrientationWatchDog.setOnOrientationListener(new ScreenOrientationWatch.OnOrientationListener() {
            @Override
            public void changedToLandScape(boolean fromPort) {
                playerChangedToLandForwardScape(fromPort);
            }

            @Override
            public void changedToLandReverseScape(boolean fromPort) {
                playerChangedToLandReverseScape(fromPort);
            }

            @Override
            public void changedToPortrait(boolean fromLand) {
                playerChangedToPortrait(fromLand);
            }
        });
    }

    /**
     * 初始化手势的控制类
     */
    private void initGestureDialogManager() {
        Context context = getContext();
        if (context instanceof Activity) {
            mGestureDialogManager = new GestureDialogManager((Activity) context);
        }
    }

    public void setPlaySource(String uri) {
        if (TextUtils.isEmpty(uri) || mAliyunVodPlayer == null) {
            return;
        }
        UrlSource urlSource = new UrlSource();
        urlSource.setUri(uri);
        //默认是5000
        int maxDelayTime = 5000;
        if (uri.startsWith("artp")) {//  //如果url的开头是artp，将直播延迟设置成100，
            maxDelayTime = 100;
        }
        PlayerConfig playerConfig = mAliyunVodPlayer.getConfig();
        if (playerConfig != null) {
            playerConfig.mMaxDelayTime = maxDelayTime;
            //开启SEI事件通知
            playerConfig.mEnableSEI = true;
            mAliyunVodPlayer.setConfig(playerConfig);
            setLocalSource(urlSource);
        }
    }

    /**
     * 更新UI播放器的主题
     *
     * @param theme 支持的主题
     */
    @Override
    public void setTheme(ThemeEnum theme) {
        //通过判断子View是否实现了ITheme的接口，去更新主题
        int childCounts = getChildCount();
        for (int i = 0; i < childCounts; i++) {
            View view = getChildAt(i);
            if (view instanceof ITheme) {
                ((ITheme) view).setTheme(theme);
            }
        }
    }

    /**
     * 隐藏手势和控制栏
     */
    private void hideGestureAndControlViews() {
        if (mGestureView != null) {
            mGestureView.hide(ViewAction.HideType.Normal);
        }
        if (mControlView != null) {
            mControlView.hide(ViewAction.HideType.Normal);
        }
    }

    /**
     * 切换播放速度
     *
     * @param speedValue 播放速度
     */
    public void changeSpeed(SpeedEnum speedValue) {
        if (speedValue == SpeedEnum.One) {
            currentSpeed = 1.0f;
        } else if (speedValue == SpeedEnum.OneQuartern) {
            currentSpeed = 1.25f;
        } else if (speedValue == SpeedEnum.OneHalf) {
            currentSpeed = 1.5f;
        } else if (speedValue == SpeedEnum.Twice) {
            currentSpeed = 2.0f;
        }
        mAliyunVodPlayer.setSpeed(currentSpeed);
    }

    /**
     * 音量 0-100
     *
     * @param progress
     */
    public void setCurrentVolume(int progress) {
        this.currentVolume = progress;
        mAliyunVodPlayer.setVolume(progress / 100.00f);
    }


    private void wifiTo4G() {
        Log.d(TAG, "onWifiTo4G");

        //如果已经显示错误了，那么就不用显示网络变化的提示了。
        if (mTipsView.isErrorShow()) {
            return;
        }

        //wifi变成4G，先暂停播放
        if (!isLocalSource()) {
            pause();
        }

        //隐藏其他的动作,防止点击界面去进行其他操作
        mGestureView.hide(ControlView.HideType.Normal);
        mControlView.hide(ControlView.HideType.Normal);

        //显示网络变化的提示
        if (!isLocalSource() && mTipsView != null) {
            mTipsView.showNetChangeTipView();
        }
    }

    private void o4GToWifi() {
        Log.d(TAG, "on4GToWifi");
        //如果已经显示错误了，那么就不用显示网络变化的提示了。
        if (mTipsView.isErrorShow()) {
            return;
        }

        //隐藏网络变化的提示
        if (mTipsView != null) {
            mTipsView.hideNetErrorTipView();
        }
    }

    /**
     * 屏幕方向变为横屏。
     *
     * @param fromPort 是否从竖屏变过来
     */
    private void playerChangedToLandForwardScape(boolean fromPort) {
        //如果不是从竖屏变过来，也就是一直是横屏的时候，就不用操作了
        if (fromPort) {
            changeScreenMode(ScreenMode.Full, false);
        }
    }

    /**
     * 屏幕方向变为横屏。
     *
     * @param fromPort 是否从竖屏变过来
     */
    private void playerChangedToLandReverseScape(boolean fromPort) {
        //如果不是从竖屏变过来，也就是一直是横屏的时候，就不用操作了
        if (fromPort) {
            changeScreenMode(ScreenMode.Full, true);
        }
    }

    /**
     * 屏幕方向变为竖屏
     *
     * @param fromLand 是否从横屏转过来
     */
    private void playerChangedToPortrait(boolean fromLand) {
        //屏幕转为竖屏
        if (mIsFullScreenLocked) {
            return;
        }
        if (mCurrentScreenMode == ScreenMode.Full) {
            //全屏情况转到了竖屏
            if (lockPortrait == LockPortrait.FIX_NONE) {
                //没有固定竖屏，就变化mode
                if (fromLand) {
                    changeScreenMode(ScreenMode.Small, false);
                } else {
                    //如果没有转到过横屏，就不让他转了。防止竖屏的时候点横屏之后，又立即转回来的现象
                }
            } else {
                //固定竖屏了，竖屏还是竖屏，不用动
            }
        } else if (mCurrentScreenMode == ScreenMode.Small) {
            //竖屏的情况转到了竖屏
        }
    }

    /**
     * 重试播放，会从当前位置开始播放
     */
    public void reTry() {
        isCompleted = false;
        inSeek = false;

        int currentPosition = mControlView.getVideoPosition();
        VcPlayerLog.d(TAG, " currentPosition = " + currentPosition);

        if (mTipsView != null) {
            mTipsView.hideAll();
        }
        if (mControlView != null) {
            mControlView.reset();
            //防止被reset掉，下次还可以获取到这些值
            mControlView.setVideoPosition(currentPosition);
        }
        if (mGestureView != null) {
            mGestureView.reset();
        }

        if (mAliyunVodPlayer != null) {

            //显示网络加载的loading。。
            if (mTipsView != null) {
                mTipsView.showNetLoadingTipView();
            }
            /*
                isLocalSource()判断不够,有可能是sts播放,也有可能是url播放,还有可能是sd卡的视频播放,
                如果是后两者,需要走if,否则走else
            */
            if (isLocalSource()) {
                mAliyunVodPlayer.setDataSource(mUrlSource);
                mAliyunVodPlayer.prepare();
            } else {
                mAliyunVodPlayer.setDataSource(mVidSts);
                mAliyunVodPlayer.prepare();
            }
            isAutoAccurate(currentPosition);
        }
    }

    /**
     * 重播，将会从头开始播放
     */
    public void rePlay() {
        isCompleted = false;
        inSeek = false;

        if (mTipsView != null) {
            mTipsView.hideAll();
        }
        if (mControlView != null) {
            mControlView.reset();
        }
        if (mGestureView != null) {
            mGestureView.reset();
        }

        if (mAliyunVodPlayer != null) {
            //显示网络加载的loading。。
            if (mTipsView != null) {
                mTipsView.showNetLoadingTipView();
            }
            //重播是从头开始播
            mAliyunVodPlayer.prepare();
        }

    }

    /**
     * 重置。包括一些状态值，view的状态等
     */
    private void reset() {
        isCompleted = false;
        inSeek = false;
        mCurrentPosition = 0;
        mVideoBufferedPosition = 0;

        if (mTipsView != null) {
            mTipsView.hideAll();
        }
        if (mControlView != null) {
            mControlView.reset();
        }
        if (mGestureView != null) {
            mGestureView.reset();
        }
        stop();
    }

    /**
     * 显示更多设置
     */
    private void playerShowMore() {
        ShowMoreDialog showMoreDialog = new ShowMoreDialog(getContext());
        ShowMoreModel moreValue = new ShowMoreModel();
        moreValue.setScreenBrightness(currentBrightness);
        moreValue.setSpeed(currentSpeed);
        moreValue.setVolume((int) currentVolume);

        ShowMoreView showMoreView = new ShowMoreView(getContext(), moreValue);
        showMoreDialog.setContentView(showMoreView);
        showMoreDialog.show();
        showMoreView.setOnDownloadButtonClickListener(() -> ToastUtils.showShort("下载未开发"));

        showMoreView.setOnScreenCastButtonClickListener(() -> ToastUtils.showShort("投屏未开发"));

        showMoreView.setOnBarrageButtonClickListener(() -> ToastUtils.showShort("投屏未弹幕"));

        showMoreView.setOnSpeedCheckedChangedListener((group, checkedId) -> {
            // 播放速度速度切换
            if (checkedId == R.id.rb_speed_normal) {
                changeSpeed(SpeedEnum.One);
            } else if (checkedId == R.id.rb_speed_onequartern) {
                changeSpeed(SpeedEnum.OneQuartern);
            } else if (checkedId == R.id.rb_speed_onehalf) {
                changeSpeed(SpeedEnum.OneHalf);
            } else if (checkedId == R.id.rb_speed_twice) {
                changeSpeed(SpeedEnum.Twice);
            }
        });


        showMoreView.setOnLightSeekChangeListener(new ShowMoreView.OnLightSeekChangeListener() {
            @Override
            public void onStart(SeekBar seekBar) {

            }

            @Override
            public void onProgress(SeekBar seekBar, int progress, boolean fromUser) {
                if (mOutScreenBrightnessListener != null) {
                    mOutScreenBrightnessListener.setScreenBrightness(progress);
                }
            }

            @Override
            public void onStop(SeekBar seekBar) {

            }
        });

        showMoreView.setOnVoiceSeekChangeListener(new ShowMoreView.OnVoiceSeekChangeListener() {
            @Override
            public void onStart(SeekBar seekBar) {

            }

            @Override
            public void onProgress(SeekBar seekBar, int progress, boolean fromUser) {
                setCurrentVolume(progress);
            }

            @Override
            public void onStop(SeekBar seekBar) {

            }
        });
    }

    /**
     * 根据位置请求缩略图
     */
    private void requestBitmapByPosition(int targetPosition) {
        if (mThumbnailHelper != null && mThumbnailPrepareSuccess) {
            mThumbnailHelper.requestBitmapAtPosition(targetPosition);
        }
    }

    /**
     * 隐藏缩略图
     */
    private void hideThumbnailView() {
        if (mThumbnailView != null) {
            mThumbnailView.hideThumbnailView();
        }
    }

    /**
     * 显示缩略图
     */
    private void showThumbnailView() {
        if (mThumbnailView != null && mThumbnailPrepareSuccess) {
            mThumbnailView.showThumbnailView();
            //根据屏幕大小调整缩略图的大小
            ImageView thumbnailImageView = mThumbnailView.getThumbnailImageView();
            if (thumbnailImageView != null) {
                ViewGroup.LayoutParams layoutParams = thumbnailImageView.getLayoutParams();
                layoutParams.width = (DimensUtils.getWidth(getContext()) / 3);
                layoutParams.height = layoutParams.width / 2 - DimensUtils.px2dp(getContext(), 10);
                thumbnailImageView.setLayoutParams(layoutParams);
            }
        }
    }

    /**
     * 锁定屏幕。锁定屏幕后，只有锁会显示，其他都不会显示。手势也不可用
     *
     * @param lockScreen 是否锁住
     */
    public void lockScreen(boolean lockScreen) {
        mIsFullScreenLocked = lockScreen;
        if (mControlView != null) {
            mControlView.setScreenLockStatus(mIsFullScreenLocked);
        }
        if (mGestureView != null) {
            mGestureView.setScreenLockStatus(mIsFullScreenLocked);
        }
    }

    /**
     * 切换播放状态。点播播放按钮之后的操作
     */
    private void switchPlayerState() {
        if (mPlayerState == IPlayer.started) {
            pause();
        } else if (mPlayerState == IPlayer.paused || mPlayerState == IPlayer.prepared) {
            start();
        }
        if (onPlayStateBtnClickListener != null) {
            onPlayStateBtnClickListener.onPlayBtnClick(mPlayerState);
        }
    }

    /**
     * 初始化手势view
     */
    private void initGestureView() {
        mGestureView = new GestureView(getContext());
        addSubView(mGestureView);
        //设置手势监听
        mGestureView.setOnGestureListener(new GestureView.GestureListener() {
            @Override
            public void onHorizontalDistance(float downX, float nowX) {
                //水平滑动调节seek。
                // seek需要在手势结束时操作。
                long duration = mAliyunVodPlayer.getDuration();
                long position = mCurrentPosition;
                long deltaPosition;
                int targetPosition = 0;
                if (mPlayerState == IPlayer.prepared ||
                        mPlayerState == IPlayer.paused ||
                        mPlayerState == IPlayer.started) {
                    //在播放时才能调整大小
                    deltaPosition = (long) (nowX - downX) * duration / getWidth();
                    targetPosition = SeekDialog.getTargetPosition(duration, position, deltaPosition);
                }
                if (mGestureDialogManager != null) {
                    inSeek = true;
                    mGestureDialogManager.showSeekDialog(AliyunVodPlayerView.this, targetPosition);
                    mControlView.setVideoPosition(targetPosition);
                    requestBitmapByPosition(targetPosition);
                    showThumbnailView();
                }
            }

            @Override
            public void onLeftVerticalDistance(float downY, float nowY) {
                //左侧上下滑动调节亮度
                int changePercent = (int) ((nowY - downY) * 100 / getHeight());
                if (mGestureDialogManager != null) {
                    int brightness = BrightnessDialog.getTargetBrightnessPercent(currentBrightness, changePercent);
                    currentBrightness = brightness;
                    mGestureDialogManager.showBrightnessDialog(AliyunVodPlayerView.this, brightness);
                }
            }

            @Override
            public void onRightVerticalDistance(float downY, float nowY) {
                //右侧上下滑动调节音量
                float volume = mAliyunVodPlayer.getVolume();
                int changePercent = (int) ((nowY - downY) * 100 / getHeight());
                if (mGestureDialogManager != null) {
                    float targetVolume = VolumeDialog.getTargetVolume(volume * 100, changePercent);
                    currentVolume = targetVolume;
                    mGestureDialogManager.showVolumeDialog(AliyunVodPlayerView.this, targetVolume);
                    //通过返回值改变音量
                    mAliyunVodPlayer.setVolume((targetVolume / 100.00f));
                }
            }

            @Override
            public void onGestureEnd() {
                //手势结束。
                //seek需要在结束时操作。
                if (mGestureDialogManager != null) {
                    mGestureDialogManager.dismissSeekDialog();
                    mGestureDialogManager.dismissBrightnessDialog();
                    mGestureDialogManager.dismissVolumeDialog();
                    if (inSeek) {
                        int seekPosition = mControlView.getVideoPosition();
                        if (seekPosition >= mAliyunVodPlayer.getDuration()) {
                            seekPosition = (int) (mAliyunVodPlayer.getDuration() - 1000);
                        }
                        if (seekPosition >= 0) {
                            seekTo(seekPosition);
                            hideThumbnailView();
                        } else {
                            inSeek = false;
                        }
                    }
                }
            }

            @Override
            public void onSingleTap() {
                //单击事件，显示控制栏
                if (mControlView != null) {
                    if (mControlView.getVisibility() != VISIBLE) {
                        mControlView.show();
                    } else {
                        mControlView.hide(ControlView.HideType.Normal);
                    }
                }
            }

            @Override
            public void onDoubleTap() {
                switchPlayerState();
            }
        });
    }


    /**
     * 判断是否是本地资源
     *
     * @return
     */
    private boolean isLocalSource() {
        if (mUrlSource != null) {
            String url = mUrlSource.getUri();
            Uri parse = Uri.parse(url);
            return parse.getScheme() == null;
        }
        return false;
    }

    /**
     * 显示错误提示
     *
     * @param errorCode  错误码
     * @param errorEvent 错误事件
     * @param errorMsg   错误描述
     */
    public void showErrorTipView(int errorCode, String errorEvent, String errorMsg) {
        stop();
        if (mTipsView != null) {
            //隐藏其他的动作,防止点击界面去进行其他操作
            mGestureView.hide(ViewAction.HideType.End);
            mControlView.hide(ViewAction.HideType.End);
            mCoverView.setVisibility(GONE);
            mTipsView.showErrorTipView(errorCode, errorEvent, errorMsg);
        }
    }

    private void hideErrorTipView() {
        if (mTipsView != null) {
            //隐藏其他的动作,防止点击界面去进行其他操作
            mTipsView.hideErrorTipView();
        }
    }

    /**
     * addSubView 添加子view到布局中
     *
     * @param view 子view
     */
    private void addSubView(View view) {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        //添加到布局中
        addView(view, params);
    }

    /**
     * 添加子View到布局中央
     */
    private void addSubViewByCenter(View view) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(view, params);
    }

    /**
     * 添加子View到布局中,在某个View的下方
     *
     * @param view            需要添加的View
     * @param belowTargetView 在这个View的下方
     */
    private void addSubViewBelow(final View view, final View belowTargetView) {
        belowTargetView.post(new Runnable() {
            @Override
            public void run() {
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                int measuredHeight = belowTargetView.getMeasuredHeight();
                params.topMargin = measuredHeight;
                //添加到布局中
                addView(view, params);
            }
        });
    }


    /**
     * 改变屏幕模式：小屏或者全屏。
     *
     * @param targetMode
     */
    public void changeScreenMode(ScreenMode targetMode, boolean isReverse) {
        Log.d(TAG, "mIsFullScreenLocked = " + mIsFullScreenLocked + " ， targetMode = " + targetMode);

        ScreenMode finalScreenMode = targetMode;

        if (mIsFullScreenLocked) {
            finalScreenMode = ScreenMode.Full;
        }

        //这里可能会对模式做一些修改
        if (targetMode != mCurrentScreenMode) {
            mCurrentScreenMode = finalScreenMode;
        }

        if (mControlView != null) {
            mControlView.setScreenModeStatus(finalScreenMode);
        }

        if (mSpeedView != null) {
            mSpeedView.setScreenMode(finalScreenMode);
        }

        if (mGuideView != null) {
            mGuideView.setScreenMode(finalScreenMode);
        }

        Context context = getContext();
        if (context instanceof Activity) {
            if (finalScreenMode == ScreenMode.Full) {
                if (lockPortrait == LockPortrait.FIX_NONE) {
                    //不是固定竖屏播放。
                    if (isReverse) {
                        ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                    } else {
                        ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }
                    //SCREEN_ORIENTATION_LANDSCAPE只能固定一个横屏方向
                } else {
                    //如果是固定全屏，那么直接设置view的布局，宽高
                    ViewGroup.LayoutParams aliVcVideoViewLayoutParams = getLayoutParams();
                    aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                }
            } else if (finalScreenMode == ScreenMode.Small) {
                if (lockPortrait == LockPortrait.FIX_NONE) {
                    //不是固定竖屏播放。
                    ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    //如果是固定全屏，那么直接设置view的布局，宽高
                    ViewGroup.LayoutParams aliVcVideoViewLayoutParams = getLayoutParams();
                    aliVcVideoViewLayoutParams.height = (int) (DimensUtils.getWidth(context) * 9.0f / 16);
                    aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                }
            }
        }
    }

    /**
     * 获取当前屏幕模式：小屏、全屏
     *
     * @return 当前屏幕模式
     */
    public ScreenMode getScreenMode() {
        return mCurrentScreenMode;
    }

    /**
     * 设置准备事件监听
     *
     * @param onPreparedListener 准备事件
     */
    public void setOnPreparedListener(IPlayer.OnPreparedListener onPreparedListener) {
        mOutPreparedListener = onPreparedListener;
    }

    /**
     * 设置错误事件监听
     *
     * @param onErrorListener 错误事件监听
     */
    public void setOnErrorListener(IPlayer.OnErrorListener onErrorListener) {
        mOutErrorListener = onErrorListener;
    }

    /**
     * 设置信息事件监听
     *
     * @param onInfoListener 信息事件监听
     */
    public void setOnInfoListener(IPlayer.OnInfoListener onInfoListener) {
        mOutInfoListener = onInfoListener;
    }

    /**
     * 设置播放完成事件监听
     *
     * @param onCompletionListener 播放完成事件监听
     */
    public void setOnCompletionListener(IPlayer.OnCompletionListener onCompletionListener) {
        mOutCompletionListener = onCompletionListener;
    }

    /**
     * 设置改变清晰度事件监听
     *
     * @param l 清晰度事件监听
     */
    public void setOnChangeQualityListener(OnChangeQualityListener l) {
        mOutChangeQualityListener = l;
    }

    /**
     * 设置自动播放事件监听
     *
     * @param l 自动播放事件监听
     */
    public void setOnAutoPlayListener(OnAutoPlayListener l) {
        mOutAutoPlayListener = l;
    }

    /**
     * 设置源超时监听
     *
     * @param l 源超时监听
     */
    public void setOnTimeExpiredErrorListener(OnTimeExpiredErrorListener l) {
        mOutTimeExpiredErrorListener = l;
    }

    /**
     * 设置鉴权过期监听，在鉴权过期前一分钟回调
     *
     * @param listener
     */
//    public void setOnUrlTimeExpiredListener(IPlayer.OnUrlTimeExpiredListener listener) {
//        this.mOutUrlTimeExpiredListener = listener;
//    }

    /**
     * 设置首帧显示事件监听
     *
     * @param onFirstFrameStartListener 首帧显示事件监听
     */
    public void setOnFirstFrameStartListener(IPlayer.OnRenderingStartListener onFirstFrameStartListener) {
        mOutFirstFrameStartListener = onFirstFrameStartListener;
    }

    /**
     * 设置seek结束监听
     *
     * @param onSeekCompleteListener seek结束监听
     */
    public void setOnSeekCompleteListener(IPlayer.OnSeekCompleteListener onSeekCompleteListener) {
        mOuterSeekCompleteListener = onSeekCompleteListener;
    }

    /**
     * 设置停止播放监听
     *
     * @param onStoppedListener 停止播放监听
     */
    public void setOnStoppedListener(OnStoppedListener onStoppedListener) {
        this.mOnStoppedListener = onStoppedListener;
    }

    /**
     * 设置加载状态监听
     *
     * @param onLoadingListener 加载状态监听
     */
    public void setOnLoadingListener(IPlayer.OnLoadingStatusListener onLoadingListener) {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.setOnLoadingStatusListener(onLoadingListener);
        }
    }

    public void setSeiDataListener(IPlayer.OnSeiDataListener onSeiDataListener) {
        this.mOutSeiDataListener = onSeiDataListener;
    }
    /**
     * 设置缓冲监听
     *
     * @param onBufferingUpdateListener 缓冲监听
     */
//    public void setOnBufferingUpdateListener(IPlayer.OnBufferingUpdateListener onBufferingUpdateListener) {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
//        }
//    }

    /**
     * 设置视频宽高变化监听
     *
     * @param onVideoSizeChangedListener 视频宽高变化监听
     */
    public void setOnVideoSizeChangedListener(IPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener) {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
        }
    }

    /**
     * 设置循环播放开始监听
     *
     * @param onCircleStartListener 循环播放开始监听
     */
//    public void setOnCircleStartListener(IPlayer.OnCircleStartListener onCircleStartListener) {
//        if (mAliyunVodPlayer != null) {
//            mAliyunVodPlayer.setOnCircleStartListener(onCircleStartListener);
//        }
//    }

    /**
     * 设置PlayAuth的播放方式
     *
     * @param aliyunPlayAuth auth
     */
    public void setAuthInfo(VidAuth aliyunPlayAuth) {
        if (mAliyunVodPlayer == null) {
            return;
        }
        //重置界面
        clearAllSource();
        reset();

        mVidAuth = aliyunPlayAuth;

        if (mControlView != null) {
            mControlView.setForceQuality(aliyunPlayAuth.isForceQuality());
        }

        //4G的话先提示
        if (!isLocalSource() && NetUtils.is4GConnected(getContext())) {
            if (mTipsView != null) {
                mTipsView.showNetChangeTipView();
            }
        } else {
            //具体的准备操作
            prepareAuth(aliyunPlayAuth);
        }
    }

    /**
     * 通过playAuth prepare
     *
     * @param aliyunPlayAuth 源
     */
    private void prepareAuth(VidAuth aliyunPlayAuth) {
        if (mTipsView != null) {
            mTipsView.showNetLoadingTipView();
        }
        if (mControlView != null) {
            mControlView.setIsMtsSource(false);
        }
        if (mQualityView != null) {
            mQualityView.setIsMtsSource(false);
        }
        mAliyunVodPlayer.setDataSource(aliyunPlayAuth);
        mAliyunVodPlayer.prepare();
    }

    /**
     * 清空之前设置的播放源
     */
    private void clearAllSource() {
        mVidAuth = null;
        mVidSts = null;
        mUrlSource = null;
    }

    /**
     * 设置本地播放源
     *
     * @param aliyunLocalSource 本地播放源
     */
    public void setLocalSource(UrlSource aliyunLocalSource) {
        if (mAliyunVodPlayer == null) {
            return;
        }

        clearAllSource();
        reset();

        mUrlSource = aliyunLocalSource;
        if (mControlView != null) {
            mControlView.setForceQuality(true);
        }
        if (!isLocalSource() && NetUtils.is4GConnected(getContext())) {
            if (mTipsView != null) {
                mTipsView.showNetChangeTipView();
            }
        } else {
            prepareLocalSource(aliyunLocalSource);
        }
    }

    /**
     * prepare本地播放源
     *
     * @param aliyunLocalSource 本地播放源
     */
    private void prepareLocalSource(UrlSource aliyunLocalSource) {
        if (mControlView != null) {
            mControlView.setForceQuality(true);
        }
        if (mControlView != null) {
            mControlView.setIsMtsSource(false);
        }

        if (mQualityView != null) {
            mQualityView.setIsMtsSource(false);
        }
        mAliyunVodPlayer.setAutoPlay(true);
        mAliyunVodPlayer.setDataSource(aliyunLocalSource);
        mAliyunVodPlayer.prepare();
    }

    /**
     * 准备vidsts源
     *
     * @param vidSts 源
     */
    public void setVidSts(VidSts vidSts) {
        if (mAliyunVodPlayer == null) {
            return;
        }

        clearAllSource();
        reset();

        mVidSts = vidSts;

        if (mControlView != null) {
            mControlView.setForceQuality(vidSts.isForceQuality());
        }
        if (NetUtils.is4GConnected(getContext())) {
            if (mTipsView != null) {
                mTipsView.showNetChangeTipView();
            }
        } else {
            prepareVidsts(mVidSts);
        }
    }

    /**
     * 准备vidsts 源
     */
    private void prepareVidsts(VidSts vidSts) {
        if (mTipsView != null) {
            mTipsView.showNetLoadingTipView();
        }
        if (mControlView != null) {
            mControlView.setIsMtsSource(false);
        }

        if (mQualityView != null) {
            mQualityView.setIsMtsSource(false);
        }
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.setDataSource(vidSts);
            mAliyunVodPlayer.prepare();
        }
    }


    /**
     * 设置封面id
     *
     * @param resId 资源id
     */
    public void setCoverResource(int resId) {
        if (mCoverView != null) {
            mCoverView.setImageResource(resId);
            mCoverView.setVisibility(isPlaying() ? GONE : VISIBLE);
        }
    }

    /**
     * 设置缩放模式
     *
     * @param scallingMode 缩放模式
     */
    public void setVideoScalingMode(IPlayer.ScaleMode scallingMode) {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.setScaleMode(scallingMode);
        }
    }

    /**
     * 在activity调用onResume的时候调用。 解决home回来后，画面方向不对的问题
     */
    public void onResume() {
        if (mIsFullScreenLocked) {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                changeScreenMode(ScreenMode.Small, false);
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                changeScreenMode(ScreenMode.Full, false);
            }
        }

        if (mNetWatchdog != null) {
            mNetWatchdog.startWatch();
        }

        if (mOrientationWatchDog != null) {
            mOrientationWatchDog.startWatch();
        }

        //从其他界面过来的话，也要show。
//        if (mControlView != null) {
//            mControlView.show();
//        }

        //onStop中记录下来的状态，在这里恢复使用
        resumePlayerState();
    }


    /**
     * 暂停播放器的操作
     */
    public void onStop() {
        if (mNetWatchdog != null) {
            mNetWatchdog.stopWatch();
        }
        if (mOrientationWatchDog != null) {
            mOrientationWatchDog.stopWatch();
        }

        //保存播放器的状态，供resume恢复使用。
        savePlayerState();
    }

    /**
     * Activity回来后，恢复之前的状态
     */
    private void resumePlayerState() {
        if (mAliyunVodPlayer == null) {
            return;
        }
        //恢复前台后需要进行判断,如果是本地资源,则继续播放,如果是4g则给予提示,不会继续播放,否则继续播放
        if (!isLocalSource() && NetUtils.is4GConnected(getContext())) {

        } else {
            start();
        }

    }

    /**
     * 保存当前的状态，供恢复使用
     */
    private void savePlayerState() {
        if (mAliyunVodPlayer == null) {
            return;
        }
        //然后再暂停播放器
        //如果希望后台继续播放，不需要暂停的话，可以注释掉pause调用。
        pause();
    }

    /**
     * 获取媒体信息
     *
     * @return 媒体信息
     */
    public MediaInfo getMediaInfo() {
        if (mAliyunVodPlayer != null) {
            return mAliyunVodPlayer.getMediaInfo();
        }

        return null;
    }

    /**
     * 活动销毁，释放
     */
    public void onDestroy() {
        stop();
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.release();
            mAliyunVodPlayer = null;
        }

        mSurfaceView = null;
        mGestureView = null;
        mControlView = null;
        mCoverView = null;
        mGestureDialogManager = null;
        mNetWatchdog = null;
        mTipsView = null;
        mAliyunMediaInfo = null;
        if (mOrientationWatchDog != null) {
            mOrientationWatchDog.destroy();
        }
        mOrientationWatchDog = null;
        if (hasLoadEnd != null) {
            hasLoadEnd.clear();
        }
    }

    /**
     * 是否处于播放状态：start或者pause了
     *
     * @return 是否处于播放状态
     */
    public boolean isPlaying() {
        return mPlayerState == IPlayer.started;
    }

    /**
     * 获取播放器状态
     *
     * @return 播放器状态
     */
    public int getPlayerState() {
        return mPlayerState;
    }

    /**
     * 开始播放
     */
    public void start() {
        if (mControlView != null) {
            mControlView.show();
            mControlView.setPlayState(ControlView.PlayState.Playing);
        }

        if (mAliyunVodPlayer == null) {
            return;
        }

        if (mGestureView != null) {
            mGestureView.show();
        }

        if (mPlayerState == IPlayer.paused || mPlayerState == IPlayer.prepared) {
            mAliyunVodPlayer.start();
        }

    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (mControlView != null) {
            mControlView.setPlayState(ControlView.PlayState.NotPlaying);
        }

        if (mAliyunVodPlayer == null) {
            return;
        }

        if (mPlayerState == IPlayer.started || mPlayerState == IPlayer.prepared) {
            mAliyunVodPlayer.pause();
        }
    }

    /**
     * 停止播放
     */
    private void stop() {
        Boolean hasLoadedEnd = null;
        MediaInfo mediaInfo = null;
        if (mAliyunVodPlayer != null && hasLoadEnd != null) {
            mediaInfo = mAliyunVodPlayer.getMediaInfo();
            hasLoadedEnd = hasLoadEnd.get(mediaInfo);
        }

        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.stop();
        }

        if (mControlView != null) {
            mControlView.setPlayState(ControlView.PlayState.NotPlaying);
        }
        if (hasLoadEnd != null) {
            hasLoadEnd.remove(mediaInfo);
        }
    }

    /**
     * seek操作
     *
     * @param position 目标位置
     */
    public void seekTo(int position) {
        if (mAliyunVodPlayer == null) {
            return;
        }
        inSeek = true;
        realySeekToFunction(position);
    }

    private void realySeekToFunction(int position) {
        isAutoAccurate(position);
        mAliyunVodPlayer.start();
        if (mControlView != null) {
            mControlView.setPlayState(ControlView.PlayState.Playing);
        }
    }

    /**
     * 判断是否开启精准seek
     */
    private void isAutoAccurate(int position) {
        if (mAliyunVodPlayer != null) {
            int duration = (int) mAliyunVodPlayer.getDuration();
            if (duration <= ACCURATE) {
                mAliyunVodPlayer.seekTo(position, IPlayer.SeekMode.Accurate);
            } else {
                mAliyunVodPlayer.seekTo(position, IPlayer.SeekMode.Inaccurate);
            }
        }
    }

    /**
     * 设置是否显示标题栏
     *
     * @param show true:是
     */
    public void setTitleBarCanShow(boolean show) {
        if (mControlView != null) {
            mControlView.setTitleBarCanShow(show);
        }
    }

    /**
     * 设置是否显示控制栏
     *
     * @param show true:是
     */
    public void setControlBarCanShow(boolean show) {
        if (mControlView != null) {
            mControlView.setControlBarCanShow(show);
        }

    }

    /**
     * 开启底层日志
     */
    public void enableNativeLog() {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.enableLog(true);
        }
    }

    /**
     * 关闭底层日志
     */
    public void disableNativeLog() {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.enableLog(false);
        }
    }

    /**
     * 设置自动播放
     *
     * @param auto true 自动播放
     */
    public void setAutoPlay(boolean auto) {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.setAutoPlay(auto);
        }
    }

    /**
     * 设置锁定竖屏播放
     *
     * @param lockPortrait
     */
    public void setLockPortraitMode(LockPortrait lockPortrait) {
        this.lockPortrait = lockPortrait;
    }

    /**
     * 锁定竖屏
     *
     * @return 竖屏监听器
     */
    public LockPortrait getLockPortraitMode() {
        return lockPortrait;
    }

    /**
     * 让home键无效
     *
     * @param keyCode 按键
     * @param event   事件
     * @return 是否处理。
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mCurrentScreenMode == ScreenMode.Full && keyCode != KeyEvent.KEYCODE_HOME && keyCode != KeyEvent.KEYCODE_VOLUME_UP && keyCode != KeyEvent.KEYCODE_VOLUME_DOWN) {
            playerChangedToPortrait(true);
            return false;
        }
        if (mIsFullScreenLocked && (keyCode != KeyEvent.KEYCODE_HOME)) {
            return false;
        }
        return true;
    }

    /**
     * 截图功能
     *
     * @return 图片
     */
    public void snapShot() {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.snapshot();
        }
    }

    /**
     * 设置循环播放
     *
     * @param circlePlay true:循环播放
     */
    public void setCirclePlay(boolean circlePlay) {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.setLoop(circlePlay);
        }
    }

    /**
     * 设置播放时的镜像模式
     *
     * @param mode 镜像模式
     */
    public void setRenderMirrorMode(IPlayer.MirrorMode mode) {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.setMirrorMode(mode);
        }
    }

    /**
     * 设置播放时的旋转方向
     *
     * @param rotate 旋转角度
     */
    public void setRenderRotate(IPlayer.RotateMode rotate) {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.setRotateMode(rotate);
        }
    }

    /**
     * 播放按钮点击listener
     */
    public interface OnPlayStateBtnClickListener {
        void onPlayBtnClick(int playerState);
    }

    /**
     * 设置播放状态点击监听
     */
    public void setOnPlayStateBtnClickListener(OnPlayStateBtnClickListener listener) {
        this.onPlayStateBtnClickListener = listener;
    }

    private OnSeekStartListener onSeekStartListener;

    /**
     * seek开始监听
     */

    public interface OnSeekStartListener {
        void onSeekStart(int position);
    }

    public void setOnSeekStartListener(OnSeekStartListener listener) {
        this.onSeekStartListener = listener;
    }

    /**
     * 设置播放器view点击事件监听，目前只对外暴露下载按钮和投屏按钮
     */
    public void setmOnPlayerViewClickListener(
            OnPlayerViewClickListener mOnPlayerViewClickListener) {
        this.mOnPlayerViewClickListener = mOnPlayerViewClickListener;
    }

    public void setNetConnectedListener(NetStatusWatch.NetConnectedListener listener) {
        this.mNetConnectedListener = listener;
    }


    public void setOnScreenBrightnessListener(OnScreenBrightnessListener listener) {
        this.mOutScreenBrightnessListener = listener;
    }

    /**
     * 获取当前播放器正在播放的媒体信息
     */
    public MediaInfo getCurrentMediaInfo() {
        return mAliyunMediaInfo;
    }

    /** ------------------- 播放器回调 --------------------------- */

    /**
     * 原视频准备完成
     */
    private void sourceVideoPlayerPrepared() {
        //需要将mThumbnailPrepareSuccess重置,否则会出现缩略图错乱的问题
        mThumbnailPrepareSuccess = false;
        if (mThumbnailView != null) {
            mThumbnailView.setThumbnailPicture(null);
        }
        if (mAliyunVodPlayer == null) {
            return;
        }
        mAliyunMediaInfo = mAliyunVodPlayer.getMediaInfo();
        if (mAliyunMediaInfo == null) {
            return;
        }
        List<Thumbnail> thumbnailList = mAliyunMediaInfo.getThumbnailList();
        if (thumbnailList != null && thumbnailList.size() > 0) {
            mThumbnailHelper = new ThumbnailHelper(thumbnailList.get(0).mURL);
            mThumbnailHelper.setOnPrepareListener(new ThumbnailHelper.OnPrepareListener() {
                @Override
                public void onPrepareSuccess() {
                    mThumbnailPrepareSuccess = true;
                }

                @Override
                public void onPrepareFail() {
                    mThumbnailPrepareSuccess = false;
                }
            });

            mThumbnailHelper.prepare();

            mThumbnailHelper.setOnThumbnailGetListener(new ThumbnailHelper.OnThumbnailGetListener() {
                @Override
                public void onThumbnailGetSuccess(long l, ThumbnailBitmapInfo thumbnailBitmapInfo) {
                    if (thumbnailBitmapInfo != null && thumbnailBitmapInfo.getThumbnailBitmap() != null) {
                        Bitmap thumbnailBitmap = thumbnailBitmapInfo.getThumbnailBitmap();
                        mThumbnailView.setTime(DateUtils.formatMs(l));
                        mThumbnailView.setThumbnailPicture(thumbnailBitmap);
                    }
                }

                @Override
                public void onThumbnailGetFail(long l, String s) {
                }
            });
        }

        //防止服务器信息和实际不一致
        mSourceDuration = mAliyunVodPlayer.getDuration();
        mAliyunMediaInfo.setDuration((int) mSourceDuration);
        TrackInfo currentTrack = mAliyunVodPlayer.currentTrack(TrackInfo.Type.TYPE_VOD);
        String currentQuality = "FD";
        if (currentTrack != null) {
            currentQuality = currentTrack.getVodDefinition();
        }
        mControlView.setMediaInfo(mAliyunMediaInfo, currentQuality);
        mControlView.setHideType(ViewAction.HideType.Normal);
        mGestureView.setHideType(ViewAction.HideType.Normal);
        mGestureView.show();
        if (mTipsView != null) {
            mTipsView.hideNetLoadingTipView();
        }

        mSurfaceView.setVisibility(View.VISIBLE);

        //准备成功之后可以调用start方法开始播放
        if (mOutPreparedListener != null) {
            mOutPreparedListener.onPrepared();
        }
    }

    /**
     * 原视频错误监听
     */
    private void sourceVideoPlayerError(ErrorInfo errorInfo) {
        if (mTipsView != null) {
            mTipsView.hideAll();
        }
        //出错之后解锁屏幕，防止不能做其他操作，比如返回。
        lockScreen(false);

        //errorInfo.getExtra()展示为null,修改为显示errorInfo.getCode的十六进制的值
        showErrorTipView(errorInfo.getCode().getValue(), Integer.toHexString(errorInfo.getCode().getValue()), errorInfo.getMsg());

        if (mOutErrorListener != null) {
            mOutErrorListener.onError(errorInfo);
        }
    }

    /**
     * 原视频开始加载
     */
    private void sourceVideoPlayerLoadingBegin() {
        if (mTipsView != null) {
            mTipsView.showBufferLoadingTipView();
        }
    }

    /**
     * 原视频开始加载进度
     */
    private void sourceVideoPlayerLoadingProgress(int percent) {
        if (mTipsView != null) {
            //视频广告,并且广告视频在播放状态,不要展示loading
            mTipsView.updateLoadingPercent(percent);
            if (percent == 100) {
                mTipsView.hideBufferLoadingTipView();
            }
        }
    }

    /**
     * 原视频加载结束
     */
    private void sourceVideoPlayerLoadingEnd() {
        if (mTipsView != null) {
            mTipsView.hideBufferLoadingTipView();
        }
        if (isPlaying()) {
            mTipsView.hideErrorTipView();
        }
        hasLoadEnd.put(mAliyunMediaInfo, true);
        vodPlayerLoadEndHandler.sendEmptyMessage(1);
    }

    /**
     * 原视频状态改变监听
     */
    private void sourceVideoPlayerStateChanged(int newState) {
        mPlayerState = newState;
        if (newState == IPlayer.stopped) {
            if (mOnStoppedListener != null) {
                mOnStoppedListener.onStop();
            }
        } else if (newState == IPlayer.started) {
            if (mControlView != null) {
                mControlView.setPlayState(ControlView.PlayState.Playing);
            }
        }
    }

    /**
     * 原视频播放完成
     */
    private void sourceVideoPlayerCompletion() {
        inSeek = false;
        //如果当前播放资源是本地资源时, 再显示replay
        if (mTipsView != null && isLocalSource()) {
            //隐藏其他的动作,防止点击界面去进行其他操作
            mGestureView.hide(ViewAction.HideType.End);
            mControlView.hide(ViewAction.HideType.End);
            mTipsView.showReplayTipView();
        }
        if (mOutCompletionListener != null) {
            mOutCompletionListener.onCompletion();
        }
    }

    /**
     * 原视频Info
     */
    private void sourceVideoPlayerInfo(InfoBean infoBean) {
        if (infoBean.getCode() == InfoCode.AutoPlayStart) {
            //自动播放开始,需要设置播放状态
            if (mControlView != null) {
                mControlView.setPlayState(ControlView.PlayState.Playing);
            }
            if (mOutAutoPlayListener != null) {
                mOutAutoPlayListener.onAutoPlayStarted();
            }
        } else if (infoBean.getCode() == InfoCode.BufferedPosition) {
            //更新bufferedPosition
            mVideoBufferedPosition = infoBean.getExtraValue();
            mControlView.setVideoBufferPosition((int) mVideoBufferedPosition);
        } else if (infoBean.getCode() == InfoCode.CurrentPosition) {
            //更新currentPosition
            mCurrentPosition = infoBean.getExtraValue();
            long min = mCurrentPosition / 1000 / 60;
            long sec = mCurrentPosition / 1000 % 60;
            if (mControlView != null && !inSeek && mPlayerState == IPlayer.started) {
                mControlView.setVideoPosition((int) mCurrentPosition);
            }
        } else {
            if (mOutInfoListener != null) {
                mOutInfoListener.onInfo(infoBean);
            }
        }
    }

    /**
     * 原视频onVideoRenderingStart
     */
    private void sourceVideoPlayerOnVideoRenderingStart() {
        mCoverView.setVisibility(GONE);
        if (mOutFirstFrameStartListener != null) {
            mOutFirstFrameStartListener.onRenderingStart();
        }
    }

    /**
     * 原视频 trackInfoChangedSuccess
     */
    private void sourceVideoPlayerTrackInfoChangedSuccess(TrackInfo trackInfo) {
        //清晰度切换监听
        if (trackInfo.getType() == TrackInfo.Type.TYPE_VOD) {
            //切换成功后就开始播放
            mControlView.setCurrentQuality(trackInfo.getVodDefinition());
            start();
            if (mTipsView != null) {
                mTipsView.hideNetLoadingTipView();
            }
            if (mOutChangeQualityListener != null) {
                mOutChangeQualityListener.onChangeQualitySuccess(TrackInfo.Type.TYPE_VOD.name());
            }
        }
    }

    /**
     * 原视频 trackInfochangedFail
     */
    private void sourceVideoPlayerTrackInfoChangedFail(TrackInfo trackInfo, ErrorInfo errorInfo) {
        //失败的话，停止播放，通知上层
        if (mTipsView != null) {
            mTipsView.hideNetLoadingTipView();
        }
        stop();
        if (mOutChangeQualityListener != null) {
            mOutChangeQualityListener.onChangeQualityFail(0, errorInfo.getMsg());
        }
    }

    /**
     * 原视频seek完成
     */
    private void sourceVideoPlayerSeekComplete() {
        inSeek = false;
        if (mOuterSeekCompleteListener != null) {
            mOuterSeekCompleteListener.onSeekComplete();
        }
    }

    /**
     * SEI事件出现
     *
     * @param i 类型
     * @param s 内容
     */
    private void sourceVideoPlayerSeiData(int i, byte[] s) {
        if (mOutSeiDataListener != null) {
            mOutSeiDataListener.onSeiData(i, s);
        }
    }

    /**
     * 更新播放器模式
     *
     * @param activity
     */
    public void updatePlayerViewMode(Activity activity) {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

            //设置view的布局，宽高之类
            LinearLayout.LayoutParams aliVcVideoViewLayoutParams = (LinearLayout.LayoutParams) getLayoutParams();
            aliVcVideoViewLayoutParams.height = (int) (DimensUtils.getWidth(activity) * 9.0f / 16);
            aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //转到横屏了。
            //隐藏状态栏
            if (!DeviceUtils.isStrangePhone()) {
                activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
            //设置view的布局，宽高
            LinearLayout.LayoutParams aliVcVideoViewLayoutParams = (LinearLayout.LayoutParams) getLayoutParams();
            aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
    }

    /**
     * ------------------- 播放器回调 end---------------------------
     */
    private void hideSystemUI() {
        AliyunVodPlayerView.this.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
