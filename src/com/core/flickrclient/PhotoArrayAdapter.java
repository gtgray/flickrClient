package com.core.flickrclient;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoArrayAdapter extends ArrayAdapter<Photo> {
	
	private int resource;
	
	
	public PhotoArrayAdapter(Context context, int resource) {
		super(context, resource);

		this.resource = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null)
			convertView = View.inflate(getContext(), resource, null);
		
		TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
		tvTitle.setText(getItem(position).getTitle());
		
		ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
		ivPhoto.setImageBitmap(getItem(position).getPreview());
		
		return convertView;
	}
	
	

}
