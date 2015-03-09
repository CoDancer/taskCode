package com.example.taskcontrol;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * @author CodeDancer
 *
 */
public class ProgramUtil {
	public ProgramUtil() {
		icon = null;
		processName = "";
		programName = "";
		memString = "";
	}

	private Drawable icon;
	private String programName;
	private String processName;
	private String memString;

	
	public String getProgramName() {
		return programName;
	}
	
	public Drawable getIcon() {
		return icon;
	}

	public String getProcessName() {
		return processName;
	}

	public String getMemString() {
		return memString;
	}

	
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public void setIcon(Drawable loadIcon) {
		this.icon = icon;	
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public void setMemString(String str) {
		this.memString = str;
		
	}

	

}
