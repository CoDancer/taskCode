package com.example.taskcontrol;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
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
