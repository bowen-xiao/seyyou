package com.bowen.seeyou.chat;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bowen.seeyou.R;
import com.bowen.seeyou.bean.MsgInfo;
import com.bowen.seeyou.bean.MsgType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bowen.seeyou.R.id.btn_send_msg;

public class ChatRoomActivity extends Activity {

	@BindView(R.id.input_autocomplete_text_view)
	AutoCompleteTextView mInputHost;
	@BindView(R.id.tv_server_status)
	TextView             mHostStatus;
	@BindView(R.id.chat_room_list)
	ListView             mChatList;
	@BindView(R.id.input_text)
	EditText             mSendMsg;
	@BindView(btn_send_msg)
	Button               mBtnSend;
	private Socket mSocket;

	//端口ID
	private final static int port = 9632;

	//端口连接的几个状态
	private final static int CONNECTION_DEFAULT = 0;
	private final static int CONNECTION_ING     = 3;
	private final static int CONNECTION_SUCCESS = 1;
	private final static int CONNECTION_FAIL    = 2;

	//消息的几种类型
	private final static int MSG_SENDING      = 10;
	private final static int MSG_SEND_SUCCESS = 20;
	private final static int MSG_SEND_FAIL    = 30;


	//消息列表
	List<MsgInfo> msgList = new ArrayList<>();

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			if(what < MSG_SENDING -1){
				//操作连接状态的值
				handConnectionStatus(what);
				mHostStatus.setTextColor(Color.BLACK);
			}else if(what > CONNECTION_ING + 1){
				//消息发送状态的操作
				handMsgSendStatus(what);
			}
		}
	};

	//处理发送状态值处理
	private void handMsgSendStatus(int what){


	}

	//处理连接状态的值
	private void handConnectionStatus(int what){

		switch (what) {

			case CONNECTION_ING:
				mHostStatus.setText("连接中...");
				break;
			case CONNECTION_DEFAULT:
				mHostStatus.setText("连接");
				break;
			case CONNECTION_SUCCESS:
				mHostStatus.setText("连接成功");
				startReceivedMsgThread();
				break;
			case CONNECTION_FAIL:
				mHostStatus.setText("连接失败");
				mHostStatus.setTextColor(Color.RED);
				break;
		}
	}

	private Thread receivedThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_room);
		ButterKnife.bind(this);

		initView();
	}

	private void initView() {
		String [] arr={"192.168.13.119","192.168.7","192.168."};
		ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr);
		mInputHost.setAdapter(arrayAdapter);
	}

	@OnClick({R.id.tv_server_status,
			  R.id.btn_send_msg})
	public void onViewClick(View view) {

		switch (view.getId()) {
			case R.id.tv_server_status:

				//连接到服务器
				getConnection();
				break;
			case R.id.btn_send_msg:
				//连接到服务器
				if (isConnection) {
					sendStrMsg();
				} else {
					//提示信息
					showDialog();
				}
				break;
		}
	}

	//弹出提示框
	private void showDialog() {
		new AlertDialog.Builder(this).setTitle("系统提示")//设置对话框标题
									 .setMessage("请先连接到服务器！")//设置显示的内容
									 .setPositiveButton("确定",
														new DialogInterface.OnClickListener() {//添加确定按钮
										 @Override
										 public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
											 dialog.dismiss();
										 }
									 }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
			@Override
			public void onClick(DialogInterface dialog, int which) {//响应事件
				dialog.dismiss();
			}
		}).show();//在按键响应事件中显示此对话框

	}


	//向服务器发送消息
	private void sendStrMsg() {
		final String sendStr = mSendMsg.getText().toString().trim();
		if(TextUtils.isEmpty(sendStr)){return;}
		mSendMsg.setText("");
		final MsgInfo info = new MsgInfo(sendStr, MsgType.SEND, MSG_SENDING);
		msgList.add(info);
		new Thread() {
			@Override
			public void run() {
				try {
					OutputStream outputStream = mSocket.getOutputStream();
					outputStream.write((sendStr+"\r\n").getBytes());
					sendMsg(MSG_SEND_SUCCESS);
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("与服务器断开连接,程序退出");
					sendMsg(CONNECTION_FAIL);
					isConnection = false;
				}
			}
		}.start();
	}

	//去开启线程监听消息
	private void startReceivedMsgThread() {
		//e.printStackTrace();
		receivedThread = new Thread() {
			@Override
			public void run() {
				InputStream inputStream = null;
				try {
					inputStream = mSocket.getInputStream();
					byte[] bytes = new byte[2048];
					int readByte = 0;
					StringBuilder stringBuilder = new StringBuilder();
					while ((readByte = inputStream.read(bytes)) > 0) {
						String tep = new String(bytes, 0, readByte).trim();
						if (tep != null && tep.length() > 0) {
							stringBuilder.append(tep);
							System.out.println(" 服务器回复 ：" + tep);
						}
					}
				} catch (IOException e) {
					//e.printStackTrace();
					System.out.println("与服务器断开连接,程序退出");
					sendMsg(CONNECTION_FAIL);
					isConnection = false;
				}
			}
		};
		receivedThread.start();
	}

	//是否已经连接
	boolean isConnection;
	//去获取连接
	private void getConnection() {
		final String hostIp = mInputHost.getText().toString();
		//已经连接的时候
		if(isConnection || TextUtils.isEmpty(hostIp)){return;}
		sendMsg(CONNECTION_ING);
		new Thread() {
			@Override
			public void run() {
				try {
					mSocket = new Socket(hostIp, port);
					isConnection = true;
					sendMsg(CONNECTION_SUCCESS);
				} catch (IOException e) {
					e.printStackTrace();
					//连接失败
					isConnection = false;
					sendMsg(CONNECTION_FAIL);
				}
			}
		}.start();
	}

	protected void sendMsg(int msg) {
		Message message = mHandler.obtainMessage();
		message.what = msg;
		mHandler.sendMessage(message);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(isConnection && mSocket!= null){
			try {
				mSocket.getInputStream().close();
				mSocket.getOutputStream().close();
				mSocket.close();
			} catch (IOException e) {
				//关闭连接
			}
		}
	}
}
