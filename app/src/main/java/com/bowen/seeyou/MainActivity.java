package com.bowen.seeyou;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bowen.seeyou.adapter.AppListAdapter;
import com.bowen.seeyou.bean.AppBean;
import com.bowen.seeyou.chat.ChatRoomActivity;
import com.bowen.seeyou.utils.DateUtils;
import com.bowen.seeyou.utils.GetSign;
import com.bowen.seeyou.utils.ToolLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

	private AppListAdapter mAdapter;
	private AlertDialog mSignSelectDialog;
	//通知栏管理器
	private NotificationManager mNotifyMgr;

	/**
	 * A native method that is implemented by the 'native-lib' native library, which is packaged with this application.
	 */
	public native String stringFromJNI();

	// Used to load the 'native-lib' library on application startup.
	static {
		System.loadLibrary("native-lib");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		initView();
		initData();
	}

	//这是一个唯一值
	private int NOTIFICATIONS_ID = 103232;
	//显示通知栏
	private void showNotification(){
		mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		PendingIntent contentIntent = PendingIntent.getActivity(
			this, 0, new Intent(this, ChatRoomActivity.class), 0);

		Notification notification = new Notification.Builder(this)
			.setSmallIcon(R.mipmap.ic_launcher)
			.setContentTitle("My notification Title")
			.setContentText("Hello World!")
			.setContentIntent(contentIntent)
			.setAutoCancel(false)
			.build();// getNotification()
		mNotifyMgr.notify(NOTIFICATIONS_ID, notification);

		//显示进度的通知栏
		showProgressNotification();
	}

	@OnClick({
		R.id.tv_in_room
		,R.id.tv_book_tick
	})
	public void handClick(View view){
		Intent intent = new Intent(this, BookActivity.class);
		switch (view.getId()){
			case R.id.tv_in_room:
				break;
			case R.id.tv_book_tick:
				intent = new Intent(this, BookTicketHomeActivity.class);
				break;

		}
		startActivity(intent);

//		showNotification();
	}

	NotificationCompat.Builder mBuilder;
	private final String TAG = "MAIN";
	private final int PROGRESS_NOTIFICATION_ID = 123353;
	private void showProgressNotification(){
		mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setContentTitle("Picture Download")
				.setContentText("Download in progress")
				.setSmallIcon(R.mipmap.ic_launcher);
		/**设置不可手动清除**/
		//notification.flags = Notification.FLAG_NO_CLEAR;
		// Start a lengthy operation in a background thread
		new Thread(
			new Runnable() {
				@Override
				public void run() {
					int incr;
					for (incr = 0; incr <= 100; incr+=5) {
						mBuilder.setProgress(100, incr, false);
						mBuilder.setContentText("download progress : " + incr+"%");
						Notification build = mBuilder.build();
						build.flags = Notification.FLAG_NO_CLEAR;
						mNotifyMgr.notify(PROGRESS_NOTIFICATION_ID, build);
						try {
							// Sleep for 5 seconds
							Thread.sleep(5*1000);
						} catch (InterruptedException e) {
							Log.d(TAG, "sleep failure");
						}
					}
					mBuilder.setContentText("Picture download success!");
					/*mBuilder.setContentText("Download complete")//下载完成
							.setProgress(0,0,false);    //移除进度条*/
					Notification build = mBuilder.build();
					build.flags = Notification.FLAG_NO_CLEAR;
					mNotifyMgr.notify(PROGRESS_NOTIFICATION_ID, build);
				}
			}
		).start();
	}

	List<AppBean> mData = new ArrayList<>();
	//去获取数据
	private void initData() {
		//初始数据
		mAdapter = new AppListAdapter(mData);
		mListView.setAdapter(mAdapter);
		//获取签名信息
		GetSign sign = new GetSign();
		String signStr = sign.getSign(this,getPackageName());
		setTitle(signStr);
		sign.getAppList2(this)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new Action1<List<AppBean>>() {
				@Override
				public void call(List<AppBean> packageInfo) {
					//去展示数据
					if(packageInfo != null){
						mData.addAll(packageInfo);
						mAdapter.notifyDataSetChanged();
					}
				}
			});

		ToolLog.e("getBeginDate", DateUtils.getBeginDate());
		ToolLog.e("getBeginDate2", DateUtils.getBeginDate());
		ToolLog.e("getEndDate", DateUtils.getEndDate());
	}

	ListView mListView;
	private void initView() {
		mListView = (ListView) findViewById(R.id.main_list_view);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AppBean bean = mData.get(position);
//				onClickCopy(bean.toString());
//				多选
//				showSelectDialog();
//				单选
				showSignSelectDialog(position);
			}
		});
	}

	//复制功能
	public void onClickCopy(String copyStr) {
		// 从API11开始android推荐使用android.content.ClipboardManager
		// 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
		ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		// 将文本内容放到系统剪贴板里。
		cm.setText(copyStr);
		Toast.makeText(this, "复制成功，可以发给朋友们了。", Toast.LENGTH_LONG).show();
	}


	private String[] areas = new String[]{"全部","APP名称", "应用包名", "应用签名"};
	private boolean[] areaState=new boolean[]{true, false, false, false };

	private ListView areaCheckListView;
	private void showSelectDialog(){
		AlertDialog ad = new AlertDialog.Builder(MainActivity.this)
			.setTitle("选择区域")
			.setMultiChoiceItems(areas,areaState,new DialogInterface.OnMultiChoiceClickListener(){
				public void onClick(DialogInterface dialog,int whichButton, boolean isChecked){
					//点击某个区域
				}
			}).setPositiveButton("确定",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog,int whichButton){
					String s = "您选择了:";
					for (int i = 0; i < areas.length; i++){
						if (areaCheckListView.getCheckedItemPositions().get(i)){
							s += i + ":"+ areaCheckListView.getAdapter().getItem(i)+ "  ";
						}else{
							areaCheckListView.getCheckedItemPositions().get(i,false);
						}
					}
					if (areaCheckListView.getCheckedItemPositions().size() > 0){
						Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
					}else{
						//没有选择
					}
					dialog.dismiss();
				}
			}).setNegativeButton("取消", null).create();
		areaCheckListView = ad.getListView();
		ad.show();
	}

	//弹出单选框
	private void showSignSelectDialog(final int position){
		mSignSelectDialog = new AlertDialog.Builder(this)
			.setTitle("选择要复制的内容")
			.setItems(areas, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				AppBean bean = mData.get(position);
				onClickCopy(bean.toString());
				Toast.makeText(MainActivity.this, "您已经选择了: " + which + ":" + areas[which], Toast.LENGTH_LONG).show();
				dialog.dismiss();
			}
		}).show();
	}

	@Override
	protected void onDestroy() {
		//防止内存泄漏
		if(mSignSelectDialog != null && mSignSelectDialog.isShowing()){
			mSignSelectDialog.dismiss();
		}
		mSignSelectDialog = null;
		//关闭通知栏
		if(mNotifyMgr!= null){
			mNotifyMgr.cancel(NOTIFICATIONS_ID);
			mNotifyMgr.cancel(PROGRESS_NOTIFICATION_ID);
		}
		super.onDestroy();
	}
}
