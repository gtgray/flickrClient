package com.core.flickrclient;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class GalleryFragmentPagerAdapter extends FragmentStatePagerAdapter  {
	
	private ArrayList<PhotoPage> loadedPages;
	
	private int count = 4;
	
	
	public GalleryFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public void addContent(ArrayList<PhotoPage> content) {
		this.loadedPages = content;
		updateCount();
	}

	@Override
	public Fragment getItem(int position) {
		return GalleryItemFragment.init(findContentByInvertedPosition(position));
	}

	@Override
	public int getCount() {
		return count;
	}
	
	private void updateCount() {
		
		this.count = loadedPages.size() * loadedPages.get(0).getPerPage();
		notifyDataSetChanged();
	}
	
	private Photo findContentByInvertedPosition(int position) {
		
		int perPage = loadedPages.get(0).getPerPage();
		int all = loadedPages.size() * perPage;
		
		int inverted = (all - 1) - position;
		int page = inverted / perPage;
		int place = inverted - (page * perPage);
		
		return loadedPages.get(page).getPhotos().get(place);
	}

}
