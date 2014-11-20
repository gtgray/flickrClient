package com.core.flickrclient;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends FragmentActivity implements OnRefreshListener, 
																OnItemClickListener, 
																OnUpdateGuiListener {
	
	private DataHoldFragment dataHoldFragment;
	
	private PhotoArrayAdapter adapter;
	
	private int itemLayout = R.layout.main_item_layout;
	
	private SwipeRefreshLayout srlRefresh;
	private ListView lvData;
	
	private boolean recreated = true;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		
		Log.d("myLogs", "MainActivity.onCreate: --- ");
		
		dataHoldFragment = reproduceDataHoldFragment(null);
		
		srlRefresh = (SwipeRefreshLayout) findViewById(R.id.srlRefresh);
		srlRefresh.setOnRefreshListener(this);
		srlRefresh.setColorSchemeResources(R.color.clr_progress_one, R.color.clr_progress_two, R.color.clr_progress_three, R.color.clr_progress_four);
		
		adapter = new PhotoArrayAdapter(this, itemLayout);
		
		lvData = (ListView) findViewById(R.id.lvData);
		lvData.setOnItemClickListener(this);
		lvData.setDividerHeight(0);
		lvData.setCacheColorHint(0);
		lvData.setVerticalFadingEdgeEnabled(false);
		lvData.setAdapter(adapter);
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		if(recreated) {
			
			if(!fillAdapter())
				dataHoldFragment.loadPhotoList();
			
			recreated = true;
		}
		
		//		lvData.smoothScrollToPosition(0);
	}


	@Override
	protected void onStop() {
		super.onStop();
		
		recreated = false;
	}

	@Override
	public void onRefresh() {
		
		srlRefresh.setRefreshing(true);
		dataHoldFragment.loadPhotoList();
	}
	
	@Override
	public void onUpdateGui(int action, Bundle data) {
		
		switch (action) {
		
		case AppConstants.ACTION_LIST_TITLES_UPDATE:
			
			PhotoPage page = dataHoldFragment.getLastLoadedPage();
			
			if(page != null) {
				addPageToAdapter(page);
			}
			
			break;

		case AppConstants.ACTION_LIST_PREVIEW_UPDATE:
			
			adapter.notifyDataSetChanged();
			srlRefresh.setRefreshing(false);
			
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		dataHoldFragment.setCheckedAtPosition(position);
		
		Intent intent = new Intent(this, SecondActivity.class);
		intent.putParcelableArrayListExtra(AppConstants.ALLDATA, dataHoldFragment.getAllPages());
		startActivity(intent);
		
		Log.d("myLogs", "MainActivity.onItemClick: position " + position);
		
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
		
//		Log.d("myLogs", "MainActivity.reproduceDataHoldFragment: frags " + fm.getFragments());
		
		return dhFragment;
	}

	private boolean fillAdapter() {
		
		ArrayList<PhotoPage> allPages = dataHoldFragment.getAllPages();
		
		if(allPages.size() > 0) {
			
			for(int i = 0; i < allPages.size(); i++) {
				
				PhotoPage page = allPages.get(i);//(i + 1); // loaded pages numeration starts from 1
				
				if(page != null) {
					addPageToAdapter(page);
				}
			}
			return true;
		}
		return false;
	}
	
	private void addPageToAdapter(PhotoPage page) {
		
		ArrayList<Photo> photos = page.getPhotos();
		
		if(photos != null) {
			
			for(Photo p : photos) {
				
//				adapter.add(0); // straight order
				adapter.insert(p, 0); // invert order
			}
			adapter.notifyDataSetChanged();
		}
	}
	
	
}
