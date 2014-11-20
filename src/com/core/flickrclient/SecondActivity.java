package com.core.flickrclient;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class SecondActivity extends FragmentActivity implements OnUpdateGuiListener {
	
	private DataHoldFragment dataHoldFragment;
	
	public final static int INVALID_POSITION = -1;
	
	private ViewPager vpGallery;
	
	private GalleryFragmentPagerAdapter vpAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_layout);
		
		Log.d("myLogs", "SecondActivity.onCreate: --- ");
		
		Intent intent = getIntent();
		ArrayList<PhotoPage> allPages = null;
		
		if(intent != null) 
			allPages = intent.getParcelableArrayListExtra(AppConstants.ALLDATA);
		
		dataHoldFragment = reproduceDataHoldFragment(allPages);
		
		vpAdapter = new GalleryFragmentPagerAdapter(getSupportFragmentManager());
		
		vpGallery = (ViewPager) findViewById(R.id.vpGallery);
		vpGallery.setAdapter(vpAdapter);
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		vpAdapter.addContent(dataHoldFragment.getAllPages());
		vpGallery.setCurrentItem(dataHoldFragment.getCheckedPosition());
		
		Log.d("myLogs", "SecondActivity.onStart: position " + dataHoldFragment.getCheckedPosition());
	}
	
	@Override
	public void onUpdateGui(int action, Bundle data) {
		
		switch (action) {
		
		case AppConstants.ACTION_FULL_PHOTO_UPDATE:
			
//			Log.d("myLogs", "SecondActivity.onUpdateGui: --- ");
			
			vpAdapter.notifyDataSetChanged();
			
			break;	
		}
	}
	
	private DataHoldFragment reproduceDataHoldFragment(ArrayList<PhotoPage> data) {
		
		DataHoldFragment dhFragment;
		
		String tag = String.valueOf(DataHoldFragment.TAG);
		FragmentManager fm = getSupportFragmentManager();
		
		dhFragment = (DataHoldFragment) fm.findFragmentByTag(tag);
		
		if(dhFragment == null) {
			
			dhFragment = DataHoldFragment.init(data);
			
			FragmentTransaction ft = fm.beginTransaction();
			ft.add(dhFragment, tag);
			ft.commit();
		}
		
//		Log.d("myLogs", "SecondActivity.reproduceDataHoldFragment: frags " + fm.getFragments());
		
		return dhFragment;
	}
	
	
	
}
