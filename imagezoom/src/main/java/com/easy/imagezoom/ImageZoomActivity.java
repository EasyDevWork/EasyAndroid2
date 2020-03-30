package com.easy.imagezoom;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.apt.annotation.ActivityInject;
import com.easy.framework.base.BaseActivity;
import com.easy.imagezoom.databinding.ImageZoomBinding;
import com.easy.net.event.ActivityEvent;

import java.util.List;


@ActivityInject
@Route(path = "/imagezoom/ImageZoomActivity", name = "图片预览")
public class ImageZoomActivity extends BaseActivity<ImageZoomPresenter, ImageZoomBinding> implements ImageZoomView<ActivityEvent>{
    private static final String STATE_POSITION = "STATE_POSITION";

    @Autowired(name = "position")
    int pagerPosition;
    @Autowired(name = "images")
    List<String> imageUrls;
    ImagePagerAdapter mAdapter;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_POSITION, viewBind.viewPager.getCurrentItem());
    }

    @Override
    public int getLayoutId() {
        return R.layout.image_zoom;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }
    }

    @Override
    public void initView() {
        ARouter.getInstance().inject(this);
        mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), imageUrls);
        viewBind.viewPager.setAdapter(mAdapter);

        // 更新下标
        viewBind.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                updateIndicator(arg0);

            }
        });
        updateIndicator(pagerPosition);
        viewBind.viewPager.setCurrentItem(pagerPosition);
    }

    private void updateIndicator(int arg0) {
        CharSequence text = getString(R.string.viewpager_indicator, arg0 + 1, mAdapter.getCount());
        viewBind.indicator.setText(text);
    }
}
