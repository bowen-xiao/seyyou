package com.bowen.seeyou.adapter;

import android.view.View;
import android.widget.TextView;

import com.bowen.seeyou.R;

import java.util.List;

/**
 * Created by 肖稳华 on 2017/4/6.
 */

public class LogAdapter extends AbstractAdapter<String, LogAdapter.Holder> {

	//private final int mTextColor;

	//必须传递的参数
	public LogAdapter(List<String> dataList) {
		super(dataList);
		//mTextColor = Color.parseColor("#33B5E5");
	}

	@Override
	public int getItemLayout() {
		return R.layout.item_ebus_line_detail;
	}

	public void setData(List<String> dataList){
		mDataList = dataList;
		notifyDataSetChanged();
	}

	@Override
	public Holder bindView(View convertView, Holder holder) {
		holder = new Holder();
		holder.tvLineName = (TextView) convertView.findViewById(R.id.tv_line_name);
		return holder;
	}

	@Override
	public void bindData(Holder holder, String item) {
		holder.tvLineName.setText(item);
		//holder.tvLineName.setTextColor(mTextColor);
	}

	class Holder{
		TextView  tvLineName;
	}

}
