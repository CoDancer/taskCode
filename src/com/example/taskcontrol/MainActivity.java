package com.example.taskcontrol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



import android.R.id;
import android.R.integer;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Animatable;
import android.media.tv.TvContract.Programs;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	
	private static PackageManager packageManager = null;
	private static ActivityManager activityManager = null;
	
	private static List<RunningAppProcessInfo> runningProcessList = null;
	private static List<ProgramUtil> infoList = null;
	
	private static PackageUtil packageUtil = null;
	private static RunningAppProcessInfo processSelected = null;
	
	private static Button btnRefresh = null;
	private static Button btnCloseAll = null;
	
	private static RefreshHandler handler = null;
	private static ProgressDialog pd = null;
	private static ProcListAdapter procListAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnRefresh = (Button) findViewById(R.id.btn_refresh_process);
		btnCloseAll = (Button) findViewById(R.id.btn_closeall_process);
		btnRefresh.setOnClickListener(new RefreshButtonListener());
		btnCloseAll.setOnClickListener(new CloseAllButtonListener());
		
		packageManager = this.getPackageManager();
		activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		packageUtil = new PackageUtil(this);
		
		runningProcessList = new ArrayList<RunningAppProcessInfo>();
		infoList = new ArrayList<ProgramUtil>();
		
		updateProcessList();
	}
	private void updateProcessList() {
		pd = new ProgressDialog(MainActivity.this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setTitle(getString(R.string.progress_tips_title));
		pd.setMessage(getString(R.string.progress_tips_content));
		
		handler = new RefreshHandler();
		RefreshThread thread = new RefreshThread();
		thread.start();
		pd.show();
		
	}
	public class RefreshThread extends Thread{

		@Override
		public void run() {
			procListAdapter = buildProcListAdapter();
			Message msg = handler.obtainMessage();
			handler.sendMessage(msg);
		}
		
	}
	
	public ProcListAdapter buildProcListAdapter() {
		if (!runningProcessList.isEmpty()) {
			runningProcessList.clear();
		}
		if (!infoList.isEmpty()) {
			infoList.clear();
		}
		runningProcessList = activityManager.getRunningAppProcesses();
		RunningAppProcessInfo procInfo = null;
		for (Iterator<RunningAppProcessInfo> iterator = runningProcessList.iterator(); iterator.hasNext();) {
			procInfo = iterator.next();
			ProgramUtil programUtil = buildProgramUtilSimpleInfo(procInfo.pid,procInfo.processName);
			infoList.add(programUtil);
		}
		ProcListAdapter adapter = new ProcListAdapter(infoList,this);
		return adapter;
	}
	private ProgramUtil buildProgramUtilSimpleInfo(int pid, String processName) {
		ProgramUtil programUtil = new ProgramUtil();
		programUtil.setProcessName(processName);
		
		ApplicationInfo tempAppInfo = packageUtil.getApplicationInfo(processName); 
		if (tempAppInfo != null) {
			programUtil.setIcon(tempAppInfo.loadIcon(packageManager));
			programUtil.setProgramName(tempAppInfo.loadLabel(packageManager).toString());
		}
		else {
			programUtil.setIcon(getApplicationContext().getResources().getDrawable(R.drawable.unknown));
			programUtil.setProgramName(processName);
		}
		String str = getUsedMemory(pid);
		programUtil.setMemString(str);
		return programUtil;
	}
	private String getUsedMemory(int pid) {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		int[] pids = {pid};
		android.os.Debug.MemoryInfo[] memoryInfos = am.getProcessMemoryInfo(pids);
		int memorysize = memoryInfos[0].getTotalPrivateDirty();
		return "内存占用为" + memorysize +"KB";
	}
	
	public class RefreshHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			getListView().setAdapter(procListAdapter);
			pd.dismiss();
		}	
	}
	public class RefreshButtonListener implements android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {
			updateProcessList();
			

		}
	}
	public class CloseAllButtonListener implements android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int count = infoList.size();
			ProgramUtil bpu = null;
			for (int i = 0; i < count; i++) {
				bpu = infoList.get(i);
				closeOneProcess(bpu.getProcessName());
			}
		updateProcessList();
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		processSelected = runningProcessList.get(position);
		AlertButtonListener listener = new AlertButtonListener();
		Dialog alertDialog = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info)
														.setTitle(R.string.please_select)
														.setNegativeButton(R.string.kill_process,listener)
														.setNeutralButton(R.string.info_detail,listener)
														.create();
		alertDialog.show();
		super.onListItemClick(l, v, position, id);
		
	}
	
	private class AlertButtonListener implements android.content.DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			switch (which) {
			case Dialog.BUTTON_NEUTRAL:
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ProcDetailActivity.class);
				
				DetailProgramUtil programUtil = buildProgramUtilComplexInfo(processSelected.processName);
				if (programUtil == null) {
					break;
				}
				Bundle bundle = new Bundle();
				bundle.putSerializable("process_info", (Serializable)programUtil);
				intent.putExtras(bundle);
				
				startActivity(intent);
				break;
			case Dialog.BUTTON_NEGATIVE:
				closeOneProcess(processSelected.processName);
				updateProcessList();
				break;
			default:
				break;
			}
			
		}
	}
	private void closeOneProcess(String processName) {
		// TODO Auto-generated method stub
		if (processName.equals(R.string.my_package)) {
			Toast.makeText(MainActivity.this, "can't Termiate Myself", Toast.LENGTH_LONG).show();
			return;
		}
		ApplicationInfo tempAppInfo = packageUtil.getApplicationInfo(processName);
		if(tempAppInfo == null){
			return;
		}
		activityManager.killBackgroundProcesses(tempAppInfo.packageName);
	}

	public DetailProgramUtil buildProgramUtilComplexInfo(String processName) {

    	DetailProgramUtil complexProgramUtil = new DetailProgramUtil();
		ApplicationInfo tempAppInfo = packageUtil.getApplicationInfo(processName);
		if (tempAppInfo == null) {
			return null;
		}
		
		PackageInfo tempPkgInfo = null;
		try {
			tempPkgInfo = packageManager.getPackageInfo(
					tempAppInfo.packageName, 
					PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_ACTIVITIES
					| PackageManager.GET_SERVICES | PackageManager.GET_PERMISSIONS);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (tempPkgInfo == null) {
			return null;
		}
		
		complexProgramUtil.setProcessName(processName);
		complexProgramUtil.setCompanyName(getString(R.string.no_use));
		complexProgramUtil.setVersionName(tempPkgInfo.versionName);
		complexProgramUtil.setVersionCode(tempPkgInfo.versionCode);
		complexProgramUtil.setDataDir(tempAppInfo.dataDir);
		complexProgramUtil.setSourceDir(tempAppInfo.sourceDir);
		complexProgramUtil.setPackageName(tempPkgInfo.packageName);
		
		complexProgramUtil.setUserPermissions(tempPkgInfo.requestedPermissions);
		complexProgramUtil.setService(tempPkgInfo.services);
		complexProgramUtil.setActivities(tempPkgInfo.activities);
		
		return complexProgramUtil;
    }
}