package com.example.taskcontrol;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProcListAdapter extends BaseAdapter {

	List<ProgramUtil> list = new ArrayList<ProgramUtil>();
	LayoutInflater layoutInflater;
	Context context;

	public ProcListAdapter(List<ProgramUtil> infoList, Context context) {
		this.list = infoList;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		
		if(convertView == null){
			layoutInflater = LayoutInflater.from(context);
			convertView = layoutInflater.inflate(R.layout.list_item, null);
			
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.image_app);
			holder.nameText = (TextView) convertView.findViewById(R.id.name_app);
			holder.processName = (TextView) convertView.findViewById(R.id.package_app);
			holder.memInfo = (TextView) convertView.findViewById(R.id.cpumem_app);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final ProgramUtil pUtil = list.get(position);
		holder.image.setImageDrawable(pUtil.getIcon());
		holder.nameText.setText(pUtil.getProgramName());
		holder.processName.setText(pUtil.getProcessName());
		holder.memInfo.setText(pUtil.getMemString());
		return convertView;
	}
	
	public class ViewHolder {
		TextView nameText;
		TextView processName;
		TextView memInfo;
		ImageView image;
	}
}