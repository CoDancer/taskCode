package com.example.taskcontrol;

import java.lang.reflect.Method;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProcDetailActivity extends Activity{


	private static final String ATTR_PACKAGE_STATS = "PackageStats";
	private DetailProgramUtil processInfo = null;
	private static TextView textProcessName = null;
	private static TextView textProcessVersion = null;
	private static TextView textInstallDir = null;
	private static TextView textDataDir = null;
	private static TextView textPackSize = null;
	private static TextView textPermission = null;
	private static TextView textService = null;
	private static TextView textActivity = null;
	private static Button btnKillProcess = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_message);
		
		textProcessName = (TextView) findViewById(R.id.detail_process_name);
		textProcessVersion = (TextView) findViewById(R.id.detail_process_copyright);
		textDataDir = (TextView) findViewById(R.id.detail_process_data_dir);
		textInstallDir = (TextView) findViewById(R.id.detail_process_install_dir);
		textPackSize = (TextView) findViewById(R.id.detail_process_package_size);
		textPermission = (TextView) findViewById(R.id.detail_process_permission);
		textService = (TextView) findViewById(R.id.detail_process_service);
		textActivity = (TextView) findViewById(R.id.detail_process_activity);
		
		btnKillProcess = (Button) findViewById(R.id.btn_kill_process);
		
		btnKillProcess.setOnClickListener(new killButtonListener());
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		processInfo = (DetailProgramUtil) bundle.getSerializable("process_info");
		showAppInfo();
	}
	
	public void showAppInfo() {
		// TODO Auto-generated method stub
		textProcessName.setText(processInfo.getProcessName());
		textInstallDir.setText(processInfo.getSourceDir());
		textProcessVersion.setText(getString(R.string.detail_process_company) + processInfo.getCompanyName()
												+ " " + getString(R.string.detail_process_version) + 
												processInfo.getVersionName() + "(" + processInfo.getVersionCode()
												+ ")");
		
		textDataDir.setText(processInfo.getDataDir());
		textPermission.setText(processInfo.getUserPermissions());
		textService.setText(processInfo.getService());
		textActivity.setText(processInfo.getActivities());
		getpackInfo(processInfo.getPackageName());
		
	}

	public void getpackInfo(String packageName) {
		// TODO Auto-generated method stub
		PackageManager pm = getPackageManager();
		try {
			Method getPackageSizeInfo = pm.getClass().getMethod("getPackageSizeInfo", String.class,
					IPackageStatsObserver.class);
			getPackageSizeInfo.invoke(pm, packageName,new packageSizeObserver());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public class packageSizeObserver extends IPackageStatsObserver.Stub{

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			// TODO Auto-generated method stub
			Message msg = mHandler.obtainMessage(1);
			Bundle data = new Bundle();
			data.putParcelable(ATTR_PACKAGE_STATS, pStats);
			msg.setData(data);
			mHandler.sendMessage(msg);
		}

	}
	public Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			switch (msg.what) {
			case 1:
				String infoString = "";
				PackageStats newPs = msg.getData().getParcelable(ATTR_PACKAGE_STATS);
				if (newPs != null) {
					infoString += "应用程序大小：" + formatFileSize(newPs .codeSize);
					infoString += "\n数据大小：" + formatFileSize(newPs.dataSize);
					infoString += "\n 缓存大小：" + formatFileSize(newPs.cacheSize);
				}
				textPackSize.setText(infoString);
				break;

			default:
				break;
			}
		}

	};
	public String formatFileSize(long codeSize) {
		// TODO Auto-generated method stub
		String result = null;
		int sub_string = 0;
		if (codeSize >= 1073741824) {
			sub_string = String.valueOf((float) codeSize/1073741824).indexOf(".");
			result = ((float) codeSize/1073741824 + "000").substring(0 ,sub_string + 3) + "GB";
		}else if(codeSize >= 1048576) {
			sub_string = String.valueOf((float) codeSize/1048576).indexOf(".");
			result = ((float) codeSize/1048576 + "000").substring(0 ,sub_string + 3) + "MB";
		}else if(codeSize >= 1024) {
			sub_string = String.valueOf((float) codeSize/1024).indexOf(".");
			result = ((float) codeSize/1024 + "000").substring(0 ,sub_string + 3) + "KB";
		}else if (codeSize < 1024) {
			result = Long.toString(codeSize) + "B";
		}
		return result;
	}

	public class killButtonListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			PackageUtil packageUtil = new PackageUtil(ProcDetailActivity.this);
			
			if(processInfo.getProcessName().equals(R.string.my_package)){
				Toast.makeText(ProcDetailActivity.this, "can't Termiate Myself", Toast.LENGTH_LONG).show();
				return;
			}
			ApplicationInfo tempAppInfo = packageUtil.getApplicationInfo(processInfo.getProcessName());
			activityManager.killBackgroundProcesses(tempAppInfo.packageName);
			Toast.makeText(ProcDetailActivity.this, "Process is killed", Toast.LENGTH_LONG).show();
		}

	}

	
}