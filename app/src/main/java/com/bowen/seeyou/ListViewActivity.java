package com.bowen.seeyou;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bowen.seeyou.adapter.AppListAdapter;
import com.bowen.seeyou.bean.AppBean;
import com.bowen.seeyou.utils.GetSign;
import com.bowen.seeyou.view.BounceListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class ListViewActivity extends AppCompatActivity {

	@BindView(R.id.my_list_view)
	BounceListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_view);
		ButterKnife.bind(this);
		//数据初始化
		initData();
	}


	List<AppBean> mData = new ArrayList<>();
	AppListAdapter mAdapter;
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
	}
}
