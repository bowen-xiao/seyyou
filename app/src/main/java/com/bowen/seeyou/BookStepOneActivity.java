package com.bowen.seeyou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bowen.seeyou.bean.BaseResult;
import com.bowen.seeyou.network.DataEngine2;
import com.bowen.seeyou.network.RxNetWorkService;
import com.bowen.seeyou.utils.CacheUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BookStepOneActivity extends AppCompatActivity {

    @BindView(R.id.et_book_account_phone_number)
    EditText mPhoneNumber;
    @BindView(R.id.et_book_account_user_id)
    EditText mUserId;
    @BindView(R.id.et_book_account_key_code)
    EditText mKeyCode;
    @BindView(R.id.tv_book_tick_log)
    TextView mTvLog;

    //上下文对象
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_book_step_one);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 数据初始化
     */
    private void initData() {
        String phoneNumber = CacheUtils.getString(this, BookTicketHomeActivity.ACCOUNT_PHONE_NUMBER, "17051052812");
        mPhoneNumber.setText(phoneNumber);
        String keyCode = CacheUtils.getString(this, BookTicketHomeActivity.ACCOUNT_KEYCODE, "b20740c94e131278c952dfc62f40a158");
        mKeyCode.setText(keyCode);
        String userId = CacheUtils.getString(this, BookTicketHomeActivity.ACCOUNT_USERID, "179792");
        mUserId.setText(userId);
        //第一次自动检查登录状态
        checkLoginStatus(phoneNumber,userId,keyCode);
    }

    @OnClick({
            R.id.btn_test_account
            , R.id.btn_next_book
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_test_account:
                getUserInfo();
                break;
            case R.id.btn_next_book:
                nexBookPage();
                break;
        }
    }

    //去下一个页面
    private void nexBookPage() {
        Intent intent = new Intent(this, BookTicketHomeActivity.class);
        startActivity(intent);
        finish();
    }

    //去获取输入的内容
    private void getUserInfo(){
        String userId = mUserId.getText().toString().trim();
        String phoneNumber = mPhoneNumber.getText().toString().trim();
        String keyCode = mKeyCode.getText().toString().trim();
        checkLoginStatus(phoneNumber,userId,keyCode);
    }

    //自动去检查登录状态
    private void checkLoginStatus(final String phoneNumber,final  String userId,final String keyCode){

            /* userName	17051052812
                userId	179792
                keyCode	b20740c94e131278c952dfc62f40a158
                pageNo	1
                pageSize	5
                payStatus	2
*/
        Map<String, Object> params = new HashMap<>();
        params.put("userId",userId);
        params.put("userName",phoneNumber);
        params.put("keyCode",keyCode);

        params.put("pageNo",1);
        params.put("pageSize",5);
        params.put("payStatus",2);

        RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
        service.getOrderList(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("main", "getOrderList onError : " + e.getMessage());
                        //ToastUtil.showToast(mActivity,e.getMessage());
                        mTvLog.setText(e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        //订票成功
                        Log.e("main", "getOrderList result : " + s.toString());
//                        ToastUtil.showToast(mActivity,s);
                        mTvLog.setText(s);
                        if(!TextUtils.isEmpty(s)){
                            BaseResult result = JSON.parseObject(s, BaseResult.class);
                            if(result.getReturnCode().equals("500")){
                                CacheUtils.setString(mActivity,BookTicketHomeActivity.ACCOUNT_KEYCODE,keyCode);
                                CacheUtils.setString(mActivity,BookTicketHomeActivity.ACCOUNT_PHONE_NUMBER,phoneNumber);
                                CacheUtils.setString(mActivity,BookTicketHomeActivity.ACCOUNT_USERID,userId);
                            }
                        }
                    }
                });
    }
}
