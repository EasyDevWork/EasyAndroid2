package com.easy.demo.ui.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;

import com.easy.demo.R;
import com.easy.demo.ui.debug.DebugActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationHelp {
    public final static String CHAT_ID = "chat";
    public final static String CHAT_NAME = "聊天";
    public final static String SUB_ID = "sub";
    public final static String SUB_NAME = "订阅";

    public static void createChannel(Context context, String channelId, String channelName, int importance) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static Notification sendNotification(Context context, String channelId, String channelName, int importance) {
        Intent intent = new Intent(context, DebugActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(DebugActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //创建通知
        Notification.Builder nb;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            createChannel(context, channelId, channelName, importance);
            nb = new Notification.Builder(context, channelId);
        } else {
            nb = new Notification.Builder(context);
        }

        //设置通知左侧的小图标
        nb.setSmallIcon(R.drawable.icon_music2)
                //设置通知标题
                .setContentTitle("test notification " + (importance > 3 ? "import" : "common"))
                //设置通知内容
                .setContentText(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O ? "8.0" : "7.0" + " 通知内容")
                //设置点击通知后自动删除通知
                .setAutoCancel(true)
                //设置显示通知时间
                .setWhen(System.currentTimeMillis())
                //设置点击通知时的响应事件
                .setContentIntent(pendingIntent);
        ;
        return nb.build();
    }
}
