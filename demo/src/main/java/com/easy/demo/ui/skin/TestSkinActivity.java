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
import com.easy.framework.skin.AttrFactory;
import com.easy.framework.skin.SkinManager;
import com.easy.framework.skin.inter.ILoaderListener;
import com.easy.framework.skin.view_attr.DynamicAttr;
import com.easy.utils.DimensUtils;
import com.easy.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@ActivityInject
@Route(path = "/demo/TestSkinActivity", name = "换肤")
public class TestSkinActivity extends BaseActivity<EmptyPresenter, TestSkinBinding> implements EmptyView {

    private static final String SKIN_NAME = "BlackFantacy.skin";
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
        textView.setText("(动态添加)");
        int width= DimensUtils.dp2px(this,100);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, width);
        textView.setLayoutParams(param);
        textView.setTextColor(getResources().getColor(R.color.back));
        textView.setBackgroundResource(R.drawable.ic_launcher);
        textView.setTextSize(20);
        viewBind.container.addView(textView);

        List<DynamicAttr> mDynamicAttr = new ArrayList<>();
        mDynamicAttr.add(new DynamicAttr(AttrFactory.TEXT_COLOR, R.color.back));
        mDynamicAttr.add(new DynamicAttr(AttrFactory.BACKGROUND, R.drawable.ic_launcher));
        dynamicAddView(textView, mDynamicAttr);
    }
}
