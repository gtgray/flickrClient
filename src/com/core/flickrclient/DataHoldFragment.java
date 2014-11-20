package com.core.flickrclient;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;

public class DataHoldFragment extends Fragment {
	
	public final static int TAG = 123100;
	
	private OnUpdateGuiListener ougListener;
	
	private ArrayList<PhotoPage> loadedPages = new ArrayList<PhotoPage>();
	
	private boolean isLoading = false;
	
	
	public static DataHoldFragment init(ArrayList<PhotoPage> data) {
		
		DataHoldFragment dhFragment = new DataHoldFragment();
		dhFragment.setRetainInstance(true);
		
		if(data != null) {
			
			Bundle args = new Bundle();
			args.putParcelableArrayList("data", data);
			dhFragment.setArguments(args);
		}
		
		return dhFragment;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		ougListener = (OnUpdateGuiListener) getActivity();
		
		if(getArguments() != null) {
			
			loadedPages = getArguments().getParcelableArrayList("data");
		}
	}
	
	public PhotoPage getLastLoadedPage() {
		
		if(loadedPages.size() > 0)
			return loadedPages.get(loadedPages.size() - 1);
		
		return null;
	}
	
	public void rememberPage(PhotoPage page) {
		this.loadedPages.add(page);
	}
	
	public ArrayList<PhotoPage> getAllPages() {
		return loadedPages;
	}

	public void putAllPages(ArrayList<PhotoPage> allPages) {
		this.loadedPages = allPages;
	}

	public void loadPhotoList() {
		
		if(isLoading) return;
		
		HttpHelper hHelper = new HttpHelper(new Handler(), new OnServerAnswerListener() {
			
			@Override
			public void onServerAnswer(int type, int status, Bundle answer) {
				
				if(status == AppConstants.STATUS_SUCCESS) {
					
					PhotoPage photoPage = answer.getParcelable(AppConstants.PHOTO_PAGE);
					
					if(photoPage != null) {
						
						rememberPage(photoPage);
						updateListPreviews(photoPage);
						ougListener.onUpdateGui(AppConstants.ACTION_LIST_TITLES_UPDATE, null);
						isLoading = false;
						
					} else {
						Mistake mistake = answer.getParcelable(AppConstants.MISTAKE);
						
						Log.d("myLogs", "DataHoldFragment.loadPhotoList: mistake " + mistake.toString());
					}
				}
				
//				Log.d("myLogs", "DataHoldFragment.loadPhotoList: --- " + answer);
			}
		});
		
		String key = getResources().getString(R.string.flickr_id);
		hHelper.loadPhotoList(key, AppConstants.DEFAULT_IMAGES_PER_PAGE, getNextPage());
		isLoading = true;
	}
	
	private void updateListPreviews(PhotoPage page) {
		
		ArrayList<Photo> photos = page.getPhotos();
		
		for(final Photo p : photos) {
			
			HttpHelper hHelper = new HttpHelper(new Handler(), new OnServerAnswerListener() {
				
				@Override
				public void onServerAnswer(int type, int status, Bundle answer) {
					
					if(status == AppConstants.STATUS_SUCCESS) {
						
						p.setPreview((Bitmap) answer.getParcelable(AppConstants.IMAGE));
						ougListener.onUpdateGui(AppConstants.ACTION_LIST_PREVIEW_UPDATE, null);
					}
					
//					Log.d("myLogs", "DataHoldFragment.updateListPreviews: --- " + answer);
				}
			});
			
			hHelper.loadPreview(p);
		}
	}
	
	private int getNextPage() {
		return (loadedPages.size() + 1);
	}
	
	public void setCheckedAtPosition(int position) {
		
		flushChecked();
		Photo p = findPhotoByPosition(position);
		p.setChecked(true);
		
		Log.d("myLogs", "DataHoldFragment.setCheckedAtPosition: --- " + p.isChecked() + ", " + p);
	}
	
	public int getCheckedPosition() {
		
		for(PhotoPage page : loadedPages) {
			
			for(int i = 0; i < page.getPhotos().size(); i++) {
				
				Photo p = page.getPhotos().get(i);
				
				Log.d("myLogs", "DataHoldFragment.getCheckedPosition: --- " + p.isChecked() + ", " + i);
				
				if(p.isChecked()) return i + (page.getPerPage() * (page.getPage() - 1)); 
			}
		}
		return 0;
	}
	
	private void flushChecked() {
		
		for(PhotoPage page : loadedPages) {
			
			for(Photo p : page.getPhotos()) {
				
				p.setChecked(false);
			}
		}
	}
	
	private Photo findPhotoByPosition(int position) {
		
		int perPage = loadedPages.get(0).getPerPage();
		int page = position / perPage;
		int place = position - (page * perPage);
		
		return loadedPages.get(page).getPhotos().get(place);
	}
	
	
	
}
