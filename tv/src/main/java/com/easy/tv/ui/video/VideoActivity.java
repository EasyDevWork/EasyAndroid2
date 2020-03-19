package com.easy.tv.ui.video;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.common.base.CommonActivity;
import com.easy.framework.rxlifecycle.ActivityEvent;
import com.easy.tv.R;
import com.easy.tv.databinding.VideoBinding;

@ActivityInject
@Route(path = "/tv/VideoActivity", name = "视频列表")
public class VideoActivity extends CommonActivity<VideoPresenter, VideoBinding> implements VideoView<ActivityEvent> {

    @Override
    public int getLayoutId() {
        return R.layout.video;
    }

    @Override
    public void initView() {

    }
}
