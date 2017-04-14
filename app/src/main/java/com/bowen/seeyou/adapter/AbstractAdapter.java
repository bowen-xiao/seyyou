package com.bowen.seeyou.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 肖稳华 on 2017/4/6.
 */

public abstract class AbstractAdapter<T,H> extends BaseAdapter {

	//需要的参数信息
	protected  List<T> mDataList = new ArrayList<>();

	public AbstractAdapter(List<T> dataList){
		mDataList = dataList;
	}

	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public T getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		H holder = null;
		T item = getItem(position);
		if(convertView == null){
			convertView = View.inflate(parent.getContext(), getItemLayout(), null);
			holder = bindView(convertView,holder);
			convertView.setTag(holder);
		}else{
			holder = (H) convertView.getTag();
		}
		//去显示数据
		bindData(holder,item);
		return convertView;
	}

	//去设置要显示的数据视图
	public abstract int getItemLayout();

	//绑定视图
	public abstract H bindView( View convertView,H holder);

	//绑定数据
	public abstract void bindData( H holder,T Data);

}
