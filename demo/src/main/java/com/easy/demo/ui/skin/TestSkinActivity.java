package com.easy.demo.ui.skin;

import android.Manifest;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestSkinBinding;
import com.easy.demo.ui.empty.EmptyPresenter;
import com.easy.demo.ui.empty.EmptyView;
import com.easy.framework.base.BaseActivity;
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
public class TestSkinActivity extends BaseActivity<EmptyPresenter, TestSkinBinding> implements EmptyView {

    String skinPath;

    @Override
    public int getLayoutId() {
        return R.layout.test_skin;
    }

    @Override
    public void initView() {
        skinPath = Environment.getExternalStorageDirectory() + File.separator + "Black.skin";
        StringBuilder builder = new StringBuilder();
        builder.append("-1.需求请求读写文件权限").append("\n");
        builder.append("0.皮肤路径:" + skinPath).append("\n");
        builder.append("1.支持动态添加的控件换肤").append("\n");
        builder.append("2.支持XML里的控件换肤").append("\n");
        builder.append("3.支持更换文字颜色").append("\n");
        builder.append("4.支持更换背景颜色").append("\n");
        builder.append("5.支持更换背景图片").append("\n");
        viewBind.tvDescribe.setText(builder.toString());
        dynamicAddView();
    }

    public void requestPermission(View view) {
        getRxPermissions().request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .doOnDispose(() -> ToastUtils.showShort("权限被取消："))
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(aBoolean -> {
                    ToastUtils.showShort("权限是否允许：" + aBoolean);
                }, throwable -> ToastUtils.showShort("权限请求异常："));
    }

    public void officeSkin(View view) {
        SkinManager2.getInstance().clearSkin();
    }

    public void customSkin(View view) {
        boolean isOk = SkinManager2.getInstance().loadSkin(skinPath);
        if (isOk) {
            ToastUtils.showShort("换肤成功");
        } else {
            ToastUtils.showShort("请检查《权限》和《皮肤包路径》是否存在：" + skinPath);
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
