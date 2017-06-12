package com.bowen.seeyou.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.bowen.seeyou.R;
import com.bowen.seeyou.chat.ChatRoomActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by 肖稳华 on 2017/6/12.
 */

public class NotificationUtils {

	//显示通知栏
	public static Notification showNotification(Context context,int NOTIFICATIONS_ID,String title,String content){
		NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
		PendingIntent contentIntent = PendingIntent.getActivity(
			context, 0, new Intent(context, ChatRoomActivity.class), 0);

		Notification notification = new Notification.Builder(context)
			.setSmallIcon(R.mipmap.ic_launcher)
			.setContentTitle("My notification Title")
			.setContentText("Hello World!")
			.setContentIntent(contentIntent)
			.setAutoCancel(false)
			.build();// getNotification()
		mNotifyMgr.notify(NOTIFICATIONS_ID, notification);
		return notification;
	}


}
