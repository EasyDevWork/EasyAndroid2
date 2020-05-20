package com.easy.demo.ui.skin;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestSkinBinding;
import com.easy.demo.ui.empty.EmptyPresenter;
import com.easy.demo.ui.empty.EmptyView;
import com.easy.framework.base.BaseActivity;
import com.easy.framework.skin.SkinManager;
import com.easy.framework.skin.inter.ILoaderListener;
import com.easy.framework.skin.view_attr.AttrResType;
import com.easy.framework.skin.view_attr.AttrType;
import com.easy.framework.skin.view_attr.BaseAttr;
import com.easy.utils.DimensUtils;
import com.easy.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@ActivityInject
@Route(path = "/demo/TestSkinActivity", name = "换肤")
public class TestSkinActivity extends BaseActivity<EmptyPresenter, TestSkinBinding> implements EmptyView {

    private static final String SKIN_NAME = "Black.skin";
    private static final String SKIN_DIR = Environment.getExternalStorageDirectory() + File.separator + SKIN_NAME;

    @Override
    public int getLayoutId() {
        return R.layout.test_skin;
    }

    @Override
    public void initView() {
        dynamicAddView();
    }

    public void officeSkin(View view) {
        SkinManager.getInstance().restoreDefaultTheme();
    }

    public void customSkin(View view) {
        File skin = new File(SKIN_DIR);
        if (skin.exists()) {
            SkinManager.getInstance().load(skin.getAbsolutePath(), new ILoaderListener() {
                @Override
                public void onStart() {
                    Log.d("TestSkinActivity", "startloadSkin");
                }

                @Override
                public void onSuccess() {
                    Log.d("TestSkinActivity", "loadSkinSuccess");
                }

                @Override
                public void onFailed() {
                    Log.d("TestSkinActivity", "loadSkinFail");
                }
            });
        } else {
            ToastUtils.showShort("请检查" + SKIN_DIR + "是否存在");
        }
    }

    public void dynamicAddView() {
        TextView textView = new TextView(this);
        textView.setText("(背景是图片)");
        int width = DimensUtils.dp2px(this, 100);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, width);
        textView.setLayoutParams(param);
        textView.setTextColor(getResources().getColor(R.color.text_color_white));
        textView.setBackgroundResource(R.drawable.ic_launcher);
        textView.setTextSize(20);
        List<BaseAttr> mDynamicAttr = new ArrayList<>();
        mDynamicAttr.add(new BaseAttr(AttrType.TextColorAttr, AttrResType.COLOR, R.color.text_color_white));
        mDynamicAttr.add(new BaseAttr(AttrType.BackgroundAttr, AttrResType.DRAWABLE, R.drawable.ic_launcher));
        dynamicAddView(textView, mDynamicAttr);
        viewBind.container.addView(textView);

        TextView textView2 = new TextView(this);
        textView2.setText("(背景是颜色)");
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(width, width);
        textView2.setLayoutParams(param2);
        textView2.setTextColor(getResources().getColor(R.color.text_color_white));
        textView2.setBackgroundColor(getResources().getColor(R.color.text_background_back));
        textView2.setTextSize(20);
        viewBind.container.addView(textView2);

        List<BaseAttr> mDynamicAttr2 = new ArrayList<>();
        mDynamicAttr2.add(new BaseAttr(AttrType.TextColorAttr, AttrResType.COLOR, R.color.text_color_white));
        mDynamicAttr2.add(new BaseAttr(AttrType.BackgroundAttr, AttrResType.COLOR, R.color.text_background_back));
        dynamicAddView(textView2, mDynamicAttr2);
    }
}
