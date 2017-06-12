package com.bowen.seeyou;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bowen.seeyou.bean.BookResult;
import com.bowen.seeyou.bean.TicketNumberResult;
import com.bowen.seeyou.network.DataEngine2;
import com.bowen.seeyou.network.RxNetWorkService;
import com.bowen.seeyou.utils.DateUtils;
import com.bowen.seeyou.utils.ToolLog;

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

public class BookActivity extends AppCompatActivity {

	@BindView(R.id.tv_p38_1)
	TextView mTv38;

	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book);
		ButterKnife.bind(this);
		mHandler = new Handler();
		//是否还存活,如果页面没有关闭就去操作
		isAlive = true;
	}

	@OnClick({
		R.id.tv_p38_1
		,R.id.tv_p414_2
		,R.id.btn_book_tick
	})
	public void handClick(View view) {
		switch (view.getId()) {
			case R.id.tv_p38_1:

				break;
			case R.id.tv_p414_2:
				bookTick();
				break;
			case R.id.btn_book_tick:
				checkTickNumber();
				break;
		}
	}

	//检查票的数量
	private void checkTickNumber(){

		/**
		 * customerId=179792
		 * &customerName=17051052812
		 * &keyCode=b20740c94e131278c952dfc62f40a158
		 * &lineId=41650&vehTime=0725&beginDate=20170610&endDate=20170630
		**/
		Map<String, Object> params = new HashMap<>();
		params.put("customerId","179792");
		params.put("customerName","17051052812");
		params.put("keyCode","b20740c94e131278c952dfc62f40a158");
		params.put("lineId","41650");
		params.put("vehTime","0725");
		//开始天数和结束天数
		params.put("beginDate",DateUtils.getBeginDate());
		params.put("endDate",DateUtils.getEndDate());
		RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
		service.checkTick(params)
			   .subscribeOn(Schedulers.io())
			   .observeOn(AndroidSchedulers.mainThread())
			   .subscribe(new Subscriber<TicketNumberResult>() {

				   @Override
				   public void onCompleted() {

				   }

				   @Override
				   public void onError(Throwable e) {

				   }

				   @Override
				   public void onNext(TicketNumberResult s) {
					   //订票成功
					   Log.e("main","check result : " + s.toString());
					   if(s != null && s.getReturnCode().equals("500")){
						   //正确返回
						   String tickets = s.getReturnData().getTickets();
						   getBuyParam(tickets);
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
						dateList.add(dayInfo);
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
				String price = String.valueOf(dateList.size() * 5.0f);
				buyDate = buyDate.substring(1,buyDate.length() -1);
				ToolLog.e("buyDate22",buyDate);
				ToolLog.e("price22",price);
				//根据价格购买
				bookTickByDate(buyDate,price);
			}else{
				runCheck();
			}
		}
	}

	//去检查时间
	private void runCheck(){
		java.util.Random random=new java.util.Random();
		int result= random.nextInt(200) + 550;// 返回[0,10)集合中的整数，注意不包括10
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Calendar instance = Calendar.getInstance();
				//当前是多少点
				int hourOfDay = instance.get(Calendar.HOUR_OF_DAY);
				ToolLog.e("hourOfDay",String.valueOf(hourOfDay));
				if(hourOfDay >= 12){
					if(isAlive){
						checkTickNumber();
					}
				}else{
					runCheck();
				}
			}
		},result);
	}

	boolean isAlive = true;
	@Override
	protected void onDestroy() {
		isAlive = false;
		super.onDestroy();
	}

	//去访问网络，订票
	private void bookTick() {
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
		params.put("userId","179792");
		params.put("userName","17051052812");
		params.put("keyCode","b20740c94e131278c952dfc62f40a158");
		//线路ID号
		params.put("lineId","41651");
		//始发站的时间 1755 = 17:55 0725 = 07:25
		params.put("vehTime","1755");
		//上车站的时间
		params.put("startTime","1802");
		//上车站的ID编号
		params.put("onStationId","7383");
		//下车站的编号
		params.put("offStationId","7577");
		//付款方式，3为深圳通 2 微信 ，1支付宝
		params.put("payType","3");
		// 深圳通ID号
		params.put("sztNo","362317893");

		//需要配置的2个参数
		//1）乘车日期
		params.put("saleDates","2017-06-28,2017-06-29");
		//2)车票总价，天数 * 票价
		params.put("tradePrice","10.0");

		RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
		service.bookTicket(params)
			   .subscribeOn(Schedulers.io())
			   .observeOn(AndroidSchedulers.mainThread())
			   .subscribe(new Subscriber<BookResult>() {

				   @Override
				   public void onCompleted() {

				   }

				   @Override
				   public void onError(Throwable e) {

				   }

				   @Override
				   public void onNext(BookResult s) {
					   //订票成功
						if(s != null && s.getReturnCode().equals("500")){
							Toast.makeText(BookActivity.this, "预订成功", Toast.LENGTH_SHORT).show();
						}else{
							//bookTick();
						}
				   }
			   });
	}



	//去访问网络，订票  414-1 的信息
	private void bookTickByDate(String dates,String prices) {
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
		params.put("userId","179792");
		params.put("userName","17051052812");
		params.put("keyCode","b20740c94e131278c952dfc62f40a158");
		//线路ID号
		params.put("lineId","41650");
		//始发站的时间 1755 = 17:55 0725 = 07:25
		params.put("vehTime","0725");
		//上车站的时间
		params.put("startTime","0725");
		//上车站的ID编号
		params.put("onStationId","2146");
		//下车站的编号
		params.put("offStationId","512033");
		//付款方式，3为深圳通 2 微信 ，1支付宝
		params.put("payType","3");
		// 深圳通ID号
		params.put("sztNo","362317893");

		//需要配置的2个参数
		//1）乘车日期
		params.put("saleDates",dates);
		//2)车票总价，天数 * 票价
		params.put("tradePrice",prices);

		RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
		service.bookTicket(params)
			   .subscribeOn(Schedulers.io())
			   .observeOn(AndroidSchedulers.mainThread())
			   .subscribe(new Subscriber<BookResult>() {

				   @Override
				   public void onCompleted() {

				   }

				   @Override
				   public void onError(Throwable e) {

				   }

				   @Override
				   public void onNext(BookResult s) {
					   //订票成功
					   if(s != null && s.getReturnCode().equals("500")){
						   Toast.makeText(BookActivity.this, "预订成功", Toast.LENGTH_SHORT).show();
					   }else{
						   //bookTick();
					   }
				   }
			   });
	}

}
