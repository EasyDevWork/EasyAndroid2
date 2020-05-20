package com.easy.demo.ui.skin;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.framework.base.SkinActivity;
import com.easy.framework.skin.SkinManager2;
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
public class TestSkinActivity extends SkinActivity {

    TextView tvDescribe;
    LinearLayout container;
    String skinPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_skin);
        tvDescribe = findViewById(R.id.tvDescribe);
        container = findViewById(R.id.container);

        skinPath = Environment.getExternalStorageDirectory() + File.separator + "Black.skin";
        initView();
    }

    public void initView() {
        StringBuilder builder = new StringBuilder();
        builder.append("-1.需求请求读写文件权限").append("\n");
        builder.append("0.皮肤路径:" + skinPath).append("\n");
        builder.append("1.支持动态添加的控件换肤").append("\n");
        builder.append("2.支持XML里的控件换肤").append("\n");
        builder.append("3.支持更换文字颜色").append("\n");
        builder.append("4.支持更换背景颜色").append("\n");
        builder.append("5.支持更换背景图片").append("\n");
        tvDescribe.setText(builder.toString());
        dynamicAddView();
    }

    public void requestPermission(View view) {
        //todo
    }

    public void officeSkin(View view) {
        SkinManager2.getInstance().clearSkin();
    }

    public void customSkin(View view) {
        boolean isOk = SkinManager2.getInstance().loadSkin(skinPath);
        if (isOk) {
            ToastUtils.showShort("换肤成功");
        } else {
            ToastUtils.showShort("请检查皮肤包是否存在：" + skinPath);
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
        container.addView(textView);

        TextView textView2 = new TextView(this);
        textView2.setText("(背景是颜色)");
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(width, width);
        textView2.setLayoutParams(param2);
        textView2.setTextColor(getResources().getColor(R.color.text_color_white));
        textView2.setBackgroundColor(getResources().getColor(R.color.text_background_back));
        textView2.setTextSize(20);
        container.addView(textView2);

        List<BaseAttr> mDynamicAttr2 = new ArrayList<>();
        mDynamicAttr2.add(new BaseAttr(AttrType.TextColorAttr, AttrResType.COLOR, R.color.text_color_white));
        mDynamicAttr2.add(new BaseAttr(AttrType.BackgroundAttr, AttrResType.COLOR, R.color.text_background_back));
        dynamicAddView(textView2, mDynamicAttr2);
    }
}
