package com.bowen.seeyou;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bowen.seeyou.adapter.EBusLineAdapter;
import com.bowen.seeyou.adapter.LogAdapter;
import com.bowen.seeyou.bean.BookResult;
import com.bowen.seeyou.bean.SearchResult;
import com.bowen.seeyou.bean.TicketNumberResult;
import com.bowen.seeyou.network.DataEngine2;
import com.bowen.seeyou.network.RxNetWorkService;
import com.bowen.seeyou.utils.CacheUtils;
import com.bowen.seeyou.utils.DateUtils;
import com.bowen.seeyou.utils.NotificationUtils;
import com.bowen.seeyou.utils.ToastUtil;
import com.bowen.seeyou.utils.ToolLog;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BookTicketHomeActivity extends AppCompatActivity {

	public final static String ACCOUNT_PHONE_NUMBER = "account_phone_number";
	public final static String ACCOUNT_USERID = "account_userid";
	public final static String ACCOUNT_KEYCODE = "account_keycode";

	@BindView(R.id.et_book_ticket_input)
	EditText etBookTicketInput;
	@BindView(R.id.btn_search_by_key_word)
	Button   btnSearchByKeyWord;
	@BindView(R.id.book_ticket_list)
	ListView bookTicketList;

	@BindView(R.id.book_ticket_log_list)
	ListView mLogList;
	//数据
	List<String> mLogData = new ArrayList<>();
	LogAdapter mLogAdapter;

	//上下文对象
	Activity mActivity;

	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		setContentView(R.layout.activity_book_ticket_home);
		ButterKnife.bind(this);
		initData();
		initRunable();
		initView();
		//第一次进入默认搜索 414
		search();
		//设置光标默认到最后面
		etBookTicketInput.setSelection(etBookTicketInput.getText().length());
	}

	String mPhoneNumber;
	String mKeyCode;
	String mUserId;
	//初始化数据
	private void initData(){
		mPhoneNumber = (CacheUtils.getString(this,BookTicketHomeActivity.ACCOUNT_PHONE_NUMBER,"17051052812"));
		mKeyCode = (CacheUtils.getString(this,BookTicketHomeActivity.ACCOUNT_KEYCODE,"b20740c94e131278c952dfc62f40a158"));
		mUserId = (CacheUtils.getString(this,BookTicketHomeActivity.ACCOUNT_USERID,"179792"));
	}


	List<SearchResult.ReturnDataBean> mData = new ArrayList<>();
	EBusLineAdapter lineAdapter;
	//去初始化配置
	private void initView() {
		//日志的参数信息
		mLogAdapter = new LogAdapter(mLogData);
		mLogList.setAdapter(mLogAdapter);

		lineAdapter = new EBusLineAdapter(mData);
		bookTicketList.setAdapter(lineAdapter);
		bookTicketList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SearchResult.ReturnDataBean bean = mData.get(position);
//				showDialog,选择是 捡漏 ，准点抢
				ToolLog.e("click item",bean.toString());
				showSignSelectDialog(bean);
			}
		});
	}

	SearchResult.ReturnDataBean clickItem;
	private AlertDialog mSignSelectDialog;
	private String[] menus = new String[]{"取消", "25号抢下个月", "捡漏当前月"};
	boolean isGetNexMonthTicket = false;
	//弹出单选框
	private void showSignSelectDialog(final SearchResult.ReturnDataBean bean){
		clickItem = bean;
		mSignSelectDialog = new AlertDialog.Builder(this)
			.setTitle("选择要操作的项")
			.setItems(menus, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					switch (which){
						case 1:
							//准点
							isGetNexMonthTicket = true;
							isCheckBook = false;
							firstBook();
							break;
						case 2:
							//捡漏
							isGetNexMonthTicket = false;
							isCheckBook = false;
							endBook();
							break;
					}
					dialog.dismiss();
				}
			}).show();
	}

	boolean isVisabile = false;
	//初始化线程
	private void initRunable(){
		isVisabile = true;
		mHandler = new Handler();
	}

	//延时的时间
	static int fast_time = 680;
	//准点抢
	private void firstBook(){
		java.util.Random random=new java.util.Random();
		int result= random.nextInt(200) + fast_time;// 返回[0,10)集合中的整数，注意不包括10
		if(isCheckBook){return;}
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Calendar instance = Calendar.getInstance();
				//当前是多少点
				int hourOfDay = instance.get(Calendar.HOUR_OF_DAY);
				ToolLog.e("hourOfDay",String.valueOf(hourOfDay));
				/*if(hourOfDay >= 12){
					checkTickNumber();
				}else{
					firstBook();
				}*/
				checkTickNumber();
			}
		},result);
	}

	boolean isCheckBook = false;
	//余票抢
	private void endBook(){
		java.util.Random random=new java.util.Random();
		int result= random.nextInt(2000) + fast_time * 3;// 返回[0,10)集合中的整数，注意不包括10
		if(isCheckBook){return;}
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Calendar instance = Calendar.getInstance();
				//当前是多少点
				int hourOfDay = instance.get(Calendar.HOUR_OF_DAY);
				ToolLog.e("hourOfDay",String.valueOf(hourOfDay));
				// 捡漏高峰其它一般在下午 7点到晚上 11点，早上7点到发车
				checkTickNumber2();
			}
		},result);
	}

	@Override
	protected void onDestroy() {
		isVisabile = false;
		super.onDestroy();
	}

	@BindView(R.id.stop_check_ticket_number)
	Button mStopBtn;

	//搜索按钮的点击事件处理
	@OnClick({
		R.id.btn_search_by_key_word
		,R.id.stop_check_ticket_number
		,R.id.clear_log
	})
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.btn_search_by_key_word:
				//处理点击事件
				//NotificationUtils.showNotification(mActivity,100232,"测试","打开Ebus");
				//跳转到其它应用
				//jumpToEbus();
				search();
				break;
			case R.id.stop_check_ticket_number:
				//去检查票
				if(clickItem == null){
					ToastUtil.showToast(mActivity,"请选择车次信息");
					return;
				}
				isCheckBook = !isCheckBook;
				if(isGetNexMonthTicket){
					firstBook();
				}else{
					endBook();
				}
				//重置按钮
				mStopBtn.setText(isCheckBook ? "开始" : "暂停");
				break;
			case R.id.clear_log:
				//清空列表
				clearLogList();
				break;
		}
	}

	//更新数据
	private void clearLogList(){
		mLogData.clear();
		mLogAdapter.notifyDataSetChanged();
	}

	private void jumpToEbus(){
		//String pkg代表包名，String download代表下载url
		final PackageManager pm = getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage("zxzs.ppgj");
		if (null != intent) {//没有获取到intent
			startActivity(intent);
		}
	}

	@BindView(R.id.book_ticket_home_search_progress)
	View mSearch;

	private void search(){
		//关键字
		String keyWord = etBookTicketInput.getText().toString().trim();
		if(TextUtils.isEmpty(keyWord)){return;}
		mSearch.setVisibility(View.VISIBLE);
		Map<String, Object> params = new HashMap<>();
		//lineNo=414&pageNo=1&pageSize=5
		params.put("lineNo",keyWord);
		params.put("pageNo","1");
		params.put("pageSize","5");
		RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
		service.search(params)
			   .subscribeOn(Schedulers.io())
			   .observeOn(AndroidSchedulers.mainThread())
			   .subscribe(new Subscriber<SearchResult>() {

				   @Override
				   public void onCompleted() {
					   mSearch.setVisibility(View.GONE);
				   }

				   @Override
				   public void onError(Throwable e) {
					   mSearch.setVisibility(View.GONE);
					   if(e != null){
						   addLogData(e.getMessage());
					   		ToolLog.e("err",e.getMessage());
					   }
				   }

				   @Override
				   public void onNext(SearchResult result) {
					   //返回结果
						if(result != null && result.getReturnCode().equals("500")){
							List<SearchResult.ReturnDataBean> returnData = result.getReturnData();
							mData = returnData;
							lineAdapter.setData(mData);
						}
				   }
			   });
	}


	//抢票模式检查票的数量
	private void checkTickNumber(){

		/**
		 * customerId=179792
		 * &customerName=17051052812
		 * &keyCode=b20740c94e131278c952dfc62f40a158
		 * &lineId=41650&vehTime=0725&beginDate=20170610&endDate=20170630
		 **/
		Map<String, Object> params = new HashMap<>();
		params.put("customerId",mUserId);
		params.put("customerName",mPhoneNumber);
		params.put("keyCode",mKeyCode);
		params.put("lineId",clickItem.getLineId());
		params.put("vehTime",clickItem.getVehTime());
		//开始天数和结束天数
		params.put("beginDate", DateUtils.getBeginDate());
		params.put("endDate",DateUtils.getEndDate());
		//请求参数信息
		String reqMsg = "LineNumber : %s; StartTime : %s; params : %s;";
		addReqLogData(String.format(reqMsg,clickItem.getLineNo(),clickItem.getStartTime(),params.toString()));

		RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
		service.checkTick(params)
			   .subscribeOn(Schedulers.io())
			   .observeOn(AndroidSchedulers.mainThread())
			   .subscribe(new Subscriber<TicketNumberResult>() {
					boolean isToBuy = false;
				   @Override
				   public void onCompleted() {
						if(!isToBuy){
							firstBook();
						}
				   }

				   @Override
				   public void onError(Throwable e) {
					   //显示错误信息
					   if(e != null){
						   addLogData(e.getMessage());
					   		ToastUtil.showToast(mActivity,e.getMessage());
					   }
				   }

				   @Override
				   public void onNext(TicketNumberResult s) {
					   //订票成功
					   Log.e("main", "check result : " + s.toString());
					   if(s != null){
						   addLogData(s.toString());
					   }
					   if(s != null && s.getReturnCode().equals("500")){
						   //正确返回
						   String tickets = s.getReturnData().getTickets();
						   getBuyParam(tickets);
						   isToBuy = true;
					   }else{
						   isToBuy = false;
					   }
				   }
			   });
	}


	//设置参数信息
	private void getBuyParam(String tickets){
		if(!TextUtils.isEmpty(tickets)){
			String[] split = tickets.split(",");
			List<String> dateList = new ArrayList<>();
			//获取票的数量
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.MONTH,instance.get(Calendar.MONTH) + 1);
			for (int i = 0; i < split.length; i++) {
				String ticketNumber = split[i];
				instance.set(Calendar.DATE,i+1);
				String dayInfo = DateUtils.getDayInfo(instance);
				//				ToolLog.e("dayInfo",dayInfo);
				//				ToolLog.e("ticketNumber",ticketNumber);

				try {
					Integer ticketIntValue = Integer.valueOf(ticketNumber);
					//周几
					int dayWeek = instance.get(Calendar.DAY_OF_WEEK);
					if(ticketIntValue >=1){
						dateList.add(dayInfo.trim());
					}
					//不是周末就加入
					//					if(dayWeek != Calendar.SATURDAY && dayWeek != Calendar.SUNDAY){
					//						dateList.add(dayInfo);
					//					}

				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			ToolLog.e("datelist111",dateList.toString());
			ToolLog.e("price111",String.valueOf(dateList.size() * 5.0f));
			if(dateList != null && dateList.size() != 0){

				//可以去买票
				String buyDate = dateList.toString();
				StringBuilder dateStr = new StringBuilder();
				for (int i = 0; i < dateList.size(); i++) {
					String dateInfo = dateList.get(i);
					dateStr.append(dateInfo.trim());
					if(i < dateList.size()){
						dateStr.append(",");
					}
				}
				String price = String.valueOf(dateList.size() * 5.0f);
				buyDate = buyDate.substring(1,buyDate.length() -1);
				ToolLog.e("buyDate22",buyDate);
				ToolLog.e("price22",price);
				//根据价格购买
				buyDate = URLDecoder.decode(dateStr.toString().trim());
				/* 可以下单*/
				bookTickByDate(buyDate,price);
				ToastUtil.showToast(mActivity,"可以下单");
			}else{
				//检查剩余票的数量
				firstBook();
			}
		}
	}



	//去访问网络，订票  414-1 的信息
	private void bookTickByDate(final String buyDate,final String price) {
		/**
		 *   userId	179792
		 userName	17051052812
		 keyCode	b20740c94e131278c952dfc62f40a158
		 saleDates	2017-06-19,2017-06-20,2017-06-21,2017-06-22,2017-06-23
		 lineId	41651
		 vehTime	1755
		 startTime	1802
		 onStationId	7383
		 offStationId	7577
		 tradePrice	25.0
		 payType	3
		 sztNo	362317893
		 */
		Map<String, Object> params = new HashMap<>();
		params.put("userId",mUserId);
		params.put("userName",mPhoneNumber);
		params.put("keyCode",mKeyCode);
		//线路ID号
		params.put("lineId",clickItem.getLineId());
		//始发站的时间 1755 = 17:55 0725 = 07:25
		params.put("vehTime",clickItem.getVehTime());
		//上车站的时间
		params.put("startTime",clickItem.getStartTime());
		//上车站的ID编号
		params.put("onStationId",clickItem.getOnStationId());
		//下车站的编号
		params.put("offStationId",clickItem.getOffStationId());
		//付款方式，3为深圳通 2 微信 ，1支付宝
		params.put("payType","3");
		// 深圳通ID号
		params.put("sztNo","362317893");

		//需要配置的2个参数
		//1）乘车日期
		params.put("saleDates",buyDate);
		//2)车票总价，天数 * 票价
		params.put("tradePrice",price);
//		params.putAll(otherInfo);

		RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
		service.bookTicket(params)
			   .subscribeOn(Schedulers.io())
			   .observeOn(AndroidSchedulers.mainThread())
			   .subscribe(new Subscriber<BookResult>() {
				   boolean isScuccess = false;
				   @Override
				   public void onCompleted() {
						//是否预订成功,如果失败重新检查
					   if(!isScuccess){
							checkTickNumber();
						}else{
						   //跳转到EBus查询
						   jumpToEbus();
					   }
				   }

				   @Override
				   public void onError(Throwable e) {
					   if(e != null ){
						   addLogData(e.getMessage());
					   ToastUtil.showToast(mActivity,e.getMessage());
					   }
				   }

				   @Override
				   public void onNext(BookResult s) {
					   //订票成功
					   if(s != null && s.getReturnCode().equals("500")){
						   isScuccess = true;
						   Toast.makeText(mActivity, "预订成功", Toast.LENGTH_SHORT).show();
						   showSuccessNotifcaiton();
					   }else{
						   //错误信息
						   ToastUtil.showToast(mActivity,s.getReturnInfo());
					   }
					   if(s != null ){
						   addLogData(s.toString());
					   }
				   }
			   });
	}

	//显示的通知
	private int BUYSUCCESS_CODE = 12366;
	private  void showSuccessNotifcaiton(){
		Toast.makeText(mActivity, "预订成功", Toast.LENGTH_SHORT).show();
		String showMsg = "%s预订成功";
		showMsg = String.format(showMsg,clickItem.getLineNo());
		NotificationUtils.showNotification(mActivity,BUYSUCCESS_CODE,"订票成功",showMsg);
	}


	//检查票的数量
	private void checkTickNumber2(){

		/**
		 * customerId=179792
		 * &customerName=17051052812
		 * &keyCode=b20740c94e131278c952dfc62f40a158
		 * &lineId=41650&vehTime=0725&beginDate=20170610&endDate=20170630
		 **/
		Map<String, Object> params = new HashMap<>();
		//必须有用户名
		params.put("customerId",mUserId);
		params.put("customerName",mPhoneNumber);
		params.put("keyCode",mKeyCode);
		params.put("lineId",clickItem.getLineId());
		params.put("vehTime",clickItem.getVehTime());
		//开始天数和结束天数
		params.put("beginDate", DateUtils.getDayInfo2(Calendar.getInstance()));
		params.put("endDate",DateUtils.getCurrentMonthEndDate());
		//请求信息,添加到日志输出表
		String reqMsg = "LineNumber : %s; StartTime : %s; params : %s;";
		addReqLogData(String.format(reqMsg,clickItem.getLineNo(),clickItem.getStartTime(),params.toString()));

		RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
		service.checkTick(params)
			   .subscribeOn(Schedulers.io())
			   .observeOn(AndroidSchedulers.mainThread())
			   .subscribe(new Subscriber<TicketNumberResult>() {
				   boolean isToBuy = false;
				   @Override
				   public void onCompleted() {
					   if(!isToBuy){
						   endBook();
					   }
				   }

				   @Override
				   public void onError(Throwable e) {
					   if(e != null ){
						   addLogData(e.getMessage());
					   }
				   }

				   @Override
				   public void onNext(TicketNumberResult s) {
					   //订票成功
					   Log.e("main", "check result : " + s.toString());
					   if(s != null ){
						   addLogData(s.toString());
					   }
					   if(s != null && s.getReturnCode().equals("500")){
						   //正确返回
						   String tickets = s.getReturnData().getTickets();
						   getBuyParam2(tickets);
						   isToBuy = true;
					   }else{
						   isToBuy = false;
					   }
				   }
			   });
	}


	//设置参数信息
	private void getBuyParam2(String tickets){
		if(!TextUtils.isEmpty(tickets)){
			String[] split = tickets.split(",");
			List<String> dateList = new ArrayList<>();
			//获取票的数量
			Calendar instance = Calendar.getInstance();
			for (int i = 0; i < split.length; i++) {
				String ticketNumber = split[i];
				String dayInfo = DateUtils.getDayInfo(instance);
				//				ToolLog.e("dayInfo",dayInfo);
				//				ToolLog.e("ticketNumber",ticketNumber);

				try {
					Integer ticketIntValue = Integer.valueOf(ticketNumber);
					//周几
//					Calendar.SUNDAY  周末
					int dayWeek = instance.get(Calendar.DAY_OF_WEEK);
					if(ticketIntValue >=1){
						dateList.add(dayInfo);
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				//每次加一天,用于下一次计算
				instance.add(Calendar.DAY_OF_MONTH, 1);
			}
			ToolLog.e("datelist111",dateList.toString());
			ToolLog.e("price111",String.valueOf(dateList.size() * 5.0f));
			if(dateList != null && dateList.size() != 0){
				//可以去买票,需要去掉所有空格
				String buyDate = dateList.toString().replaceAll(" ","");
				String price = String.valueOf(dateList.size() * 5.0f);
				buyDate = buyDate.substring(1,buyDate.length() -1);
				ToolLog.e("buyDate22",buyDate);
				ToolLog.e("price22",price);
				//根据价格购买
				bookTickByDate(buyDate,price);
//				ToastUtil.showToast(mActivity,"捡漏"+buyDate);
			}else{
				//检查剩余票的数量
				endBook();
			}
		}
	}

	private long mLastLogTime ;
	private void addLogData(String strLog){
		if(!TextUtils.isEmpty(strLog)){
			Calendar instance = Calendar.getInstance();
			if(mLastLogTime == 0 ){
				mLastLogTime = instance.getTimeInMillis();
			}
			//间隔时间
			long spaceTime = (instance.getTimeInMillis() - mLastLogTime)/10;
			String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(instance.getTime());
			String logMsg = "查询结果; 间隔 : %s毫秒 ; 当前时间: %s \n %s";
			mLogData.add(String.format(logMsg,spaceTime,dateStr,strLog));
			mLogAdapter.notifyDataSetChanged();
			mLogList.smoothScrollToPosition(mLogData.size());
		}
	}

	private void addReqLogData(String strLog){
		if(!TextUtils.isEmpty(strLog)){
			Calendar instance = Calendar.getInstance();
			String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(instance.getTime());
			String logMsg = "请求参数信息; 当前时间: %s \n 请求参数信息 : %s";
			mLogData.add(String.format(logMsg,dateStr,strLog));
			mLogAdapter.notifyDataSetChanged();
			mLogList.smoothScrollToPosition(mLogData.size());
		}
	}

}
