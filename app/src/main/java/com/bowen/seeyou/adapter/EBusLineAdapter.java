package com.bowen.seeyou.adapter;

import android.view.View;
import android.widget.TextView;

import com.bowen.seeyou.R;
import com.bowen.seeyou.bean.SearchResult;

import java.util.List;

/**
 * Created by 肖稳华 on 2017/4/6.
 */

public class EBusLineAdapter extends AbstractAdapter<SearchResult.ReturnDataBean, EBusLineAdapter.Holder> {

	//必须传递的参数
	public EBusLineAdapter(List<SearchResult.ReturnDataBean> dataList) {
		super(dataList);
	}

	@Override
	public int getItemLayout() {
		return R.layout.item_ebus_line_detail;
	}

	public void setData(List<SearchResult.ReturnDataBean> dataList){
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
	public void bindData(Holder holder, SearchResult.ReturnDataBean item) {
		String showMsg = "线路编号: %s \n%s ===> %s  \n发车时间 : %s";
		showMsg = String.format(showMsg,item.getLineNo(),item.getOnStationName(),item.getOffStationName(),item.getStartTime());
		holder.tvLineName.setText(showMsg);
	}

	class Holder{
		TextView  tvLineName;
	}

}
