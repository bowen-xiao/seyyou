package com.bowen.seeyou.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by 肖稳华 on 2017/4/6.
 */

public class AppBean {

	private String   appName;
	private String   packageName;
	private String   installDate;
	private Drawable appLabel;
	private String signInfo;
	private long installTime;

	@Override
	public String toString() {
		return "AppBean{" +
			   "appName='" + appName + '\'' +
			   ", packageName='" + packageName + '\'' +
			   ", installDate='" + installDate + '\'' +
			   ", appLabel=" + appLabel +
			   ", signInfo='" + signInfo + '\'' +
			   ", installTime=" + installTime +
			   '}';
	}

	public long getInstallTime() {
		return installTime;
	}

	public void setInstallTime(long installTime) {
		this.installTime = installTime;
	}

	public String getSignInfo() {
		return signInfo;
	}

	public void setSignInfo(String signInfo) {
		this.signInfo = signInfo;
	}

	public String getInstallDate() {
		return installDate;
	}

	public void setInstallDate(String installDate) {
		this.installDate = installDate;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Drawable getAppLabel() {
		return appLabel;
	}

	public void setAppLabel(Drawable appLabel) {
		this.appLabel = appLabel;
	}
}
