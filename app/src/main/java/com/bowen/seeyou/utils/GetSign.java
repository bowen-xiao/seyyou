package com.bowen.seeyou.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.bowen.seeyou.bean.AppBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by 肖稳华 on 2017/4/6.
 */

public class GetSign {


	private Signature[] getRawSignature(Context paramContext, String paramString) {
		if ((paramString == null) || (paramString.length() == 0)) {
			errout("获取签名失败，包名为 null");
			return null;
		}
		PackageManager localPackageManager = paramContext.getPackageManager();
		PackageInfo localPackageInfo;
		try {
			localPackageInfo = localPackageManager.getPackageInfo(paramString, PackageManager.GET_SIGNATURES);
			if (localPackageInfo == null) {
				errout("信息为 null, 包名 = " + paramString);
				return null;
			}
		} catch (PackageManager.NameNotFoundException localNameNotFoundException) {
			errout("包名没有找到...");
			return null;
		}
		return localPackageInfo.signatures;
	}

	/**
	 * 开始获得签名
	 * @param packageName 报名
	 * @return
	 */
	public String getSign(Context context,String packageName) {
		Signature[] arrayOfSignature = getRawSignature(context, packageName);
		if ((arrayOfSignature == null) || (arrayOfSignature.length == 0)){
			errout("signs is null");
			return "";
		}

		return(MD5.getMessageDigest(arrayOfSignature[0].toByteArray()));
	}

	public Observable<List<AppBean>> getAppList2(Context context) {
		return makeObservable(getUsers(context))
			.subscribeOn(Schedulers.computation()); // note: do not use Schedulers.io()
	}

	/**
	 * 通过包名获取应用程序的名称。
	 * @param context
	 *            Context对象。
	 * @param packageName
	 *            包名。
	 * @return 返回包名所对应的应用程序的名称。
	 */
	public static String getProgramNameByPackageName(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		String name = null;
		try {
			name = pm.getApplicationLabel(
				pm.getApplicationInfo(packageName,
									  PackageManager.GET_META_DATA)).toString();
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return name;
	}


	private static <T> Observable<T> makeObservable(final Callable<T> func) {
		return Observable.create(
			new Observable.OnSubscribe<T>() {
				@Override
				public void call(Subscriber<? super T> subscriber) {
					try {
						subscriber.onNext(func.call());
					} catch(Exception ex) {
						Log.e(TAG, "Error reading from the database", ex);
					}
				}
			});
	}

	//创建带回调参数的
	Callable<List<AppBean>> getUsers(final Context context) {
		return new Callable<List<AppBean>>() {
			@Override
			public List<AppBean> call() {
				// select * from users where _id is userId
				return getAppList(context);
			}
		};
	}


	private final static String TAG = "main";
	/**
	 * 输出错误信息
	 * @param reason
	 */
	private void errout(String reason) {

		// 输出错误日志
		Log.d(TAG, "errout() called with: " + "reason = [" + reason + "]");
	}

	//获取应用信息
	public List<AppBean> getAppList(final Context context){
		//这里需要在子线程中执行
		boolean isMainThread = Thread.currentThread().getName() == "main";
		Log.e(TAG,"isMainThread : " + isMainThread + " : " + Thread.currentThread().getName());

		List<AppBean> result = new ArrayList<>();
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> pakageinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (int i = 0; i < pakageinfos.size(); i++) {
			PackageInfo info = pakageinfos.get(i);
			//第三方应用,非系统应用
			if((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0){
				AppBean appBean = new AppBean();
				Drawable loadIcon = info.applicationInfo.loadIcon(pm);
				String appName = info.applicationInfo.loadLabel(pm).toString();
				appBean.setAppLabel(loadIcon);
				appBean.setAppName(appName);
				appBean.setPackageName(info.packageName);
				appBean.setInstallDate(getDate(info.lastUpdateTime));
				appBean.setSignInfo(getSign(context,info.packageName));
				appBean.setInstallTime(info.lastUpdateTime);
				result.add(appBean);
			}
		}
		//对结果进行排序
		sortList(result);
		return result;
	}


	private <T extends AppBean> void sortList(List<T> data){
		Collections.sort(data, new Comparator<AppBean>() {
			@Override
			public int compare(AppBean o1, AppBean o2) {
				//按安装时间降序
				if(o1.getInstallTime() > o2.getInstallTime()){
					return -1;
				}else if(o1.getInstallTime() < o2.getInstallTime()){
					return 1;
				}
				return 0;
			}
		});
	}

	//获取时间日期
	private String getDate(long date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);
		String created = calendar.get(Calendar.YEAR) + "年"
						 + (calendar.get(Calendar.MONTH)+1) + "月"//从0计算
						 + calendar.get(Calendar.DAY_OF_MONTH) + "日"
						 + calendar.get(Calendar.HOUR_OF_DAY) + "时"
						 + calendar.get(Calendar.MINUTE) + "分"+calendar.get(Calendar.SECOND)+"s";
		return created;
	}

}
