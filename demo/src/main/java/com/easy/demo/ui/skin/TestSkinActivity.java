package com.easy.demo.ui.skin;

import android.Manifest;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.bean.DebugDo;
import com.easy.demo.databinding.TestSkinBinding;
import com.easy.framework.base.BaseActivity;
import com.easy.framework.skin.SkinManager;
import com.easy.framework.skin.SkinResourcesHelp;
import com.easy.framework.skin.view_attr.AttrType;
import com.easy.framework.skin.view_attr.SkinAttrParam;
import com.easy.utils.DimensUtils;
import com.easy.utils.LanguageUtil;
import com.easy.utils.ToastUtils;

import java.io.File;

@ActivityInject
@Route(path = "/demo/TestSkinActivity", name = "换肤")
public class TestSkinActivity extends BaseActivity<TestSkinPresenter, TestSkinBinding> implements TestSkinView {

    String skinPath;
    int width;
    Typeface typeface;
    String skinApkName = "Black.skin";
    int i;

    @Override
    public int getLayoutId() {
        return R.layout.test_skin;
    }

    @Override
    public void initView() {
        width = DimensUtils.dp2px(this, 100);
        skinPath = Environment.getExternalStorageDirectory() + File.separator + skinApkName;

        StringBuilder builder = new StringBuilder();
        builder.append("0.操作步骤：请求读写文件权限,\n复制Asset皮肤包到sd卡（按钮上直接点击）").append("\n");
        builder.append("1.皮肤路径:" + skinPath).append("\n");
        builder.append("2.支持动态添加的控件换肤").append("\n");
        builder.append("3.支持XML里的控件换肤").append("\n");
        builder.append("4.支持更换文字颜色").append("\n");
        builder.append("5.支持更换背景颜色").append("\n");
        builder.append("6.支持更换背景图片").append("\n");
        builder.append("7.支持ImageView src 图片").append("\n");
        builder.append("8.支持drawable left/start/right/end/top/bottom").append("\n");
        builder.append("9.支持修改字体").append("\n");
        builder.append("10.支持切换语言").append("\n");
        viewBind.tvDescribe.setText(builder.toString());

        typeface = Typeface.createFromAsset(context.getAssets(), getString(R.string.font_type_face));
        viewBind.tvDescribe.setTypeface(typeface);

        SkinAttrParam src = new SkinAttrParam(AttrType.TYPE_FACE, R.string.font_type_face);
        SkinManager.getInstance().addSkinView(this, viewBind.tvDescribe, src);

        dynamicAddView();
    }

    public void copySkinApk(View view) {
        showLoading();
        presenter.copySkinApkToSdcard(skinApkName, skinPath);
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
        SkinManager.getInstance().loadSkin(null, (loadState, throwable) -> {
            if (loadState) {
                ToastUtils.showShort("换肤成功");
            } else {
                //请检查《权限》和《皮肤包路径》是否存在：+skinPath
                ToastUtils.showShort("定制失败：\n" + throwable.getMessage());
            }
        });
    }

    public void changeLanguage(View v) {
        i++;
        if (i % 2 == 0) {
            SkinResourcesHelp.getInstance().changeLanguage("zh");
            ToastUtils.showShort("切换到中文");
        } else {
            SkinResourcesHelp.getInstance().changeLanguage("en");
            ToastUtils.showShort("切换到英文");
        }
        Log.d("Language", "current1:" + LanguageUtil.getSystemLanguage());
    }

    public void clearSkin(View view) {
        SkinManager.getInstance().clearSkin();
    }

    public void customSkin(View view) {
        SkinManager.getInstance().loadSkin(skinPath, (loadState, throwable) -> {
            if (loadState) {
                ToastUtils.showShort("换肤成功");
            } else {
                //请检查《权限》和《皮肤包路径》是否存在：+skinPath
                ToastUtils.showShort("定制失败：\n" + throwable.getMessage());
            }
        });
    }

    public void dynamicAddView() {
        TextView textView = new TextView(this);
        textView.setText("(背景是图片)");
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, width);
        param.gravity = Gravity.CENTER;
        textView.setLayoutParams(param);
        textView.setTextColor(getResources().getColor(R.color.text_color_white));
        textView.setBackgroundResource(R.drawable.ic_launcher);
        textView.setTextSize(20);
        viewBind.container.addView(textView);

        SkinAttrParam background = new SkinAttrParam(AttrType.BACKGROUND, R.drawable.ic_launcher);
        SkinAttrParam textColor = new SkinAttrParam(AttrType.TEXT_COLOR, R.color.text_color_white);
        SkinManager.getInstance().addSkinView(this, textView, background, textColor);

        TextView textView2 = new TextView(this);
        textView2.setText("(背景是颜色)");
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(width, width);
        param2.gravity = Gravity.CENTER;
        textView2.setLayoutParams(param2);
        textView2.setTextColor(getResources().getColor(R.color.text_color_white));
        textView2.setBackgroundColor(getResources().getColor(R.color.text_background_back));
        textView2.setTextSize(20);
        viewBind.container.addView(textView2);

        SkinAttrParam background2 = new SkinAttrParam(AttrType.BACKGROUND, R.color.text_background_back);
        SkinAttrParam textColor2 = new SkinAttrParam(AttrType.TEXT_COLOR, R.color.text_color_white);
        SkinManager.getInstance().addSkinView(this, textView2, background2, textColor2);

        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(width, width);
        imageView.setLayoutParams(param3);
        imageView.setImageResource(R.drawable.ic_launcher);
        viewBind.container.addView(imageView);

        SkinAttrParam src = new SkinAttrParam(AttrType.SRC, R.drawable.ic_launcher);
        SkinManager.getInstance().addSkinView(this, imageView, src);
    }

    @Override
    public void copyCallback(boolean result) {
        hideLoading();
        ToastUtils.showShort("复制结果：" + result);
    }
}
