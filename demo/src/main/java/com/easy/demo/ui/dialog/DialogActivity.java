package com.easy.demo.ui.dialog;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestDialogBinding;
import com.easy.framework.base.BaseActivity;
import com.easy.net.event.ActivityEvent;
import com.easy.utils.ToastUtils;
import com.lxj.xpopup.XPopup;

@ActivityInject
@Route(path = "/demo/DialogActivity", name = "对话框测试")
public class DialogActivity extends BaseActivity<DialogPresenter, TestDialogBinding> implements DialogView<ActivityEvent> {
    XPopup.Builder builder;

    @Override
    public int getLayoutId() {
        return R.layout.test_dialog;
    }

    @Override
    public void initView() {
        // 必须在事件发生前，调用这个方法来监视View的触摸
        builder = new XPopup.Builder(this).watchView(viewBind.btn3);
    }


    public void dialog1(View view) {
        new XPopup.Builder(this).asConfirm("我是标题", "我是内容",
                () -> ToastUtils.showShort("click confirm"))
                .show();
    }

    public void dialog2(View view) {
        new XPopup.Builder(this)
                .asBottomList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4", "条目5"},
                        (position, text) -> ToastUtils.showShort("click confirm"))
                .show();
    }

    public void dialog3(View view) {
        builder.asAttachList(new String[]{"置顶", "复制", "删除"}, null,
                (position, text) -> ToastUtils.showShort("click " + text))
                .show();
    }

    public void dialog4(View view) {
        // 多图片场景
        //srcView参数表示你点击的那个ImageView，动画从它开始，结束时回到它的位置。
        //注意：如果你自己的ImageView的scaleType是centerCrop类型的，你加载图片需要指定Original_Size，禁止Glide裁剪图片。
//        new XPopup.Builder(this).asImageViewer(imageView, position, list, new OnSrcViewUpdateListener() {
//            @Override
//            public void onSrcViewUpdate(ImageViewerPopupView popupView, int position) {
//                // 作用是当Pager切换了图片，需要更新源View
//                popupView.updateSrcView((ImageView) recyclerView.getChildAt(position));
//            }
//        }, new PopupImageLoader())
//                .show();

        // 单张图片场景
        new XPopup.Builder(this)
                .asImageViewer(viewBind.ivImage, "http://i1.sinaimg.cn/ent/d/2008-06-04/U105P28T3D2048907F326DT20080604225106.jpg", new PopupImageLoader())
                .show();
    }

    public void dialog5(View view) {
        new XPopup.Builder(this)
                .asLoading("正在加载中")
                .show();
    }
}
