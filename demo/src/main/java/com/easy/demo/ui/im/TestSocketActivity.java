package com.easy.demo.ui.im;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestSocketBinding;
import com.easy.demo.ui.empty.EmptyPresenter;
import com.easy.demo.ui.empty.EmptyView;
import com.easy.framework.base.BaseActivity;
import com.freddy.im.CThreadPoolExecutor;
import com.freddy.im.IMSClientBootstrap;
import com.freddy.im.MessageProcessor;
import com.freddy.im.MessageType;
import com.freddy.im.bean.SingleMessage;
import com.freddy.im.event.CEventCenter;
import com.freddy.im.event.Events;
import com.freddy.im.event.I_CEventListener;

import java.util.UUID;

@ActivityInject
@Route(path = "/demo/TestSocketActivity", name = "空页面")
public class TestSocketActivity extends BaseActivity<EmptyPresenter, TestSocketBinding> implements EmptyView, I_CEventListener {

    String userId = "100002";
    String token = "token_" + userId;
    String hosts = "[{\"host\":\"192.168.8.25\", \"port\":8856}]";
    String toUser = "100001";
    private static final String[] EVENTS = {
            Events.CHAT_SINGLE_MESSAGE
    };

    @Override
    public int getLayoutId() {
        return R.layout.test_socket;
    }

    @Override
    public void initView() {
        IMSClientBootstrap.getInstance().init(userId, token, hosts, 1);

        CEventCenter.registerEventListener(this, EVENTS);
    }

    public void sendMsg(View view) {
        SingleMessage message = new SingleMessage();
        message.setMsgId(UUID.randomUUID().toString());
        message.setMsgType(MessageType.SINGLE_CHAT.getMsgType());
        message.setMsgContentType(MessageType.MessageContentType.TEXT.getMsgContentType());
        message.setFromId(userId);
        message.setToId(toUser);
        message.setTimestamp(System.currentTimeMillis());
        message.setContent(viewBind.etContent.getText().toString());
        MessageProcessor.getInstance().sendMsg(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CEventCenter.unregisterEventListener(this, EVENTS);
    }

    @Override
    public void onCEvent(String topic, int msgCode, int resultCode, Object obj) {
        switch (topic) {
            case Events.CHAT_SINGLE_MESSAGE: {
                final SingleMessage message = (SingleMessage) obj;
                CThreadPoolExecutor.runOnMainThread(() -> viewBind.tvMsg.setText(message.getContent()));
                break;
            }
            default:
                break;
        }
    }
}
