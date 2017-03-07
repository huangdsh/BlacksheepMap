package com.hds.blacksheepmap;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.hds.blacksheepmap.view.FeatureView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * 高德定位SDK功能接口实例 更多SDK请进入官网“http://lbs.amap.com/api/”查看
 * 官方论坛：http://lbsbbs.amap.com/portal.php
 *
 * @项目名称： AMapLocationDemo2.x
 * 
 * @author hongming.wang
 * @文件名称: StartActivity.java
 * @类型名称: StartActivity
 */
public class StartActivity extends ListActivity {
	private static class DemoDetails {
		private final int titleId;
		private final int descriptionId;
		private final Class<? extends android.app.Activity> activityClass;
		public DemoDetails(int titleId, int descriptionId,
				Class<? extends android.app.Activity> activityClass) {
			super();
			this.titleId = titleId;
			this.descriptionId = descriptionId;
			this.activityClass = activityClass;
		}
	}

	private static class CustomArrayAdapter extends ArrayAdapter<DemoDetails> {
		public CustomArrayAdapter(Context context, DemoDetails[] demos) {
			super(context, R.layout.feature, R.id.title, demos);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FeatureView featureView;
			if (convertView instanceof FeatureView) {
				featureView = (FeatureView) convertView;
			} else {
				featureView = new FeatureView(getContext());
			}
			DemoDetails demo = getItem(position);
			featureView.setTitleId(demo.titleId);
			featureView.setDescriptionId(demo.descriptionId);
			return featureView;
		}
	}

	private static final DemoDetails[] demos = {
			new DemoDetails(R.string.location,
					R.string.location_dec, Location_Activity.class),
			new DemoDetails(R.string.geoFence, R.string.geoFence_dec,
					GeoFence_Activity.class),
			new DemoDetails(R.string.assistantLocation,
					R.string.assistantLocation_dec,
					Assistant_Location_Activity.class),
			new DemoDetails(R.string.tools, R.string.tools_dec,
					Tools_Activity.class),
			new DemoDetails(R.string.lastLocation, R.string.lastLocation_dec,
					LastLocation_Activity.class),
			new DemoDetails(R.string.alarmCPU, R.string.alarmCPU_dec,
					Alarm_Location_Activity.class),
			new DemoDetails(R.string.errorCode, R.string.errorCode_dec,
					ErrorCode_Activity.class),
			};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		/**
		 * setApiKey必须在启动Activity或者Application的onCreate里进行， 在其他地方使用有可能无效,
		 * 如果使用setApiKey设置key，则AndroidManifest.xml里的key会失效
		 */
		// AMapLocationClient.setApiKey("需要更换的key");

		setTitle(R.string.title_main);
		ListAdapter adapter = new CustomArrayAdapter(
				this.getApplicationContext(), demos);
		setListAdapter(adapter);
//		permChecker = new PermissionsChecker(this);
		Log.i("xxxx", sHA1(this));
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		System.exit(0);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		DemoDetails demo = (DemoDetails) getListAdapter().getItem(position);
		startActivity(
				new Intent(this.getApplicationContext(), demo.activityClass));
	}
	public static String sHA1(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), PackageManager.GET_SIGNATURES);
			byte[] cert = info.signatures[0].toByteArray();
			MessageDigest md = MessageDigest.getInstance("SHA1");
			byte[] publicKey = md.digest(cert);
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < publicKey.length; i++) {
				String appendString = Integer.toHexString(0xFF & publicKey[i])
						.toUpperCase(Locale.US);
				if (appendString.length() == 1)
					hexString.append("0");
				hexString.append(appendString);
				hexString.append(":");
			}
			String result = hexString.toString();
			return result.substring(0, result.length()-1);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
