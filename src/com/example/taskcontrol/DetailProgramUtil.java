package com.example.taskcontrol;

import java.io.Serializable;
import java.sql.Date;

import android.content.pm.ActivityInfo;
import android.content.pm.ServiceInfo;
import android.text.format.DateFormat;

public class DetailProgramUtil implements Serializable{
	private static final long serialVersionUID = 1L;
	private String packageName;
	private int pid;
	private String processName;
	
	private String companyName;
	private int versionCode;
	private String versionName;
	
	private String dataDir;
	private String sourceDir;
	
	private String firstInstallTime;
	private String lastUpdateTime;
	
	private String userPermissions;
	private String activities;
	private String services;
	
	public DetailProgramUtil() {
		pid = 0;
		processName = "";
		companyName = "";
		versionCode = 0;
		versionName = "";
		dataDir = "";
		sourceDir = "";
		firstInstallTime = "";
		lastUpdateTime = "";
		userPermissions = "";
		activities = "";
		services = "";
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getProcessName() {
		if (processName == null || processName.length() <= 0) {
			processName = "null";
		}
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getDataDir() {
		if (dataDir == null || dataDir.length() <= 0) {
			dataDir = "null";
		}
		return dataDir;
	}

	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}

	public String getSourceDir() {
		if (sourceDir == null || sourceDir.length() <= 0) {
			sourceDir = "null";
		}
		return sourceDir;
	}

	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}

	public String getFirstInstallTime() {
		if (firstInstallTime == null || firstInstallTime.length() <= 0) {
			firstInstallTime = "null";
		}
		return firstInstallTime;
	}

	public void setFirstInstallTime(Long firstInstallTime) {
		this.firstInstallTime = DateFormat.format("yyyy-MM-dd", firstInstallTime).toString();
	}

	public String getLastUpdateTime() {
		if (lastUpdateTime == null || lastUpdateTime.length() <= 0) {
			lastUpdateTime = "null";
		}
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Long lastUpdateTime) {
		this.lastUpdateTime = DateFormat.format("yyyy-MM-dd", lastUpdateTime).toString();
	}

	public String getUserPermissions() {
		if (userPermissions == null || userPermissions.length() <= 0) {
			userPermissions = "null";
		}
		return userPermissions;
	}

	public void setUserPermissions(String[] userPermissions) {
		this.userPermissions = Array2String(userPermissions);
	}

	public String getActivities() {
		if (activities == null || activities.length() <= 0) {
			activities = "null";
		}
		return activities;
	}

	public void setActivities(ActivityInfo[] activities) {
		this.activities = Array2String(activities);
	}


	public String getService() {
		if (services == null || services.length() <= 0) {
			services = "null";
		}
		return services;
	}

	public void setService(ServiceInfo[] service) {
		this.services = Array2String(service);
	}
	
	

	public String Array2String(String[] array) {
		// TODO Auto-generated method stub
		String resultString = "";
		if (array != null && array.length > 0) {
			resultString = "";
			for(int i = 0; i < array.length ;i++){
				resultString += array[i];
				if(i < (array.length - 1)){
					resultString += "\n";
				}
			}
		}
		return resultString;
	}
	
	private String Array2String(ActivityInfo[] array) {
		// TODO Auto-generated method stub
		String resultString = "";
		if (array != null && array.length > 0) {
			resultString = "";
			for(int i = 0; i < array.length ;i++){
				if (array[i].name == null ) {
					continue;
				}
				resultString += array[i].name.toString();
				if(i < (array.length - 1)){
					resultString += "\n";
				}
			}
		}
		return resultString;
	}
	
	private String Array2String(ServiceInfo[] array) {
		// TODO Auto-generated method stub
		String resultString = "";
		if (array != null && array.length > 0) {
			resultString = "";
			for(int i = 0; i < array.length ;i++){
				if (array[i].name == null ) {
					continue;
				}
				resultString += array[i].name.toString();
				if(i < (array.length - 1)){
					resultString += "\n";
				}
			}
		}
		return resultString;
	}
	
}
