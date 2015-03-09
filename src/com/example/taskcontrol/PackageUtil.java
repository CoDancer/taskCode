package com.example.taskcontrol;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class PackageUtil {
	private List<ApplicationInfo> allAppList;

	public PackageUtil(Context context) {
		PackageManager pm = context.getApplicationContext().getPackageManager();
		allAppList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);	
	}

	public ApplicationInfo getApplicationInfo(String processName) {
		if (processName == null) {
			return null;
		}
		for(ApplicationInfo appInfo : allAppList){
			if (processName.equals(appInfo.processName)) {
				return appInfo;
			}
		}
		return null;
	}

}
