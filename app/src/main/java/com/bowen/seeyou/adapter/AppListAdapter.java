package com.bowen.seeyou.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowen.seeyou.R;
import com.bowen.seeyou.bean.AppBean;

import java.util.List;

/**
 * Created by 肖稳华 on 2017/4/6.
 */

public class AppListAdapter extends AbstractAdapter<AppBean, AppListAdapter.Holder> {

	//必须传递的参数
	public AppListAdapter(List<AppBean> dataList) {
		super(dataList);
	}

	@Override
	public int getItemLayout() {
		return R.layout.list_item_app_info;
	}

	@Override
	public Holder bindView(View convertView, Holder holder) {
		holder = new Holder();
		holder.tvAppName = (TextView) convertView.findViewById(R.id.item_list_app_name);
		holder.tvPackageName = (TextView) convertView.findViewById(R.id.item_list_package_name);
		holder.appLogo = (ImageView) convertView.findViewById(R.id.item_list_app_logo);
		holder.tvInstallDate = (TextView) convertView.findViewById(R.id.item_list_app_date);
		holder.signInfo = (TextView) convertView.findViewById(R.id.item_list_sign_info);
		return holder;
	}

	@Override
	public void bindData(Holder holder, AppBean item) {
		holder.tvAppName.setText("名称:" + item.getAppName());
		holder.appLogo.setImageDrawable(item.getAppLabel());
		holder.tvPackageName.setText("包名:" +item.getPackageName());
		holder.tvInstallDate.setText("安装:" +item.getInstallDate());
		holder.signInfo.setText("签名:" +item.getSignInfo());
	}

	class Holder{
		TextView  tvAppName;
		TextView  tvPackageName;
		TextView  tvInstallDate;
		TextView  signInfo;
		ImageView appLogo;
	}

}
