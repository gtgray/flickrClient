package com.core.flickrclient;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GalleryItemFragment extends Fragment {
	
//	public final static int TAG = 124100;
	
	private OnUpdateGuiListener ougListener;
	
	private int itemLayout = R.layout.gallery_item_layout;
	
	private Photo photo;
	
	private ImageView ivPhoto;
	
	
	public static GalleryItemFragment init(Photo data) {
		
		GalleryItemFragment giFragment = new GalleryItemFragment();
//		giFragment.setRetainInstance(true);
		
		if(data != null) {
			
			Bundle args = new Bundle();
			args.putParcelable("data", data);
			giFragment.setArguments(args);
		}
		
		return giFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View v = inflater.inflate(itemLayout, container, false);
		ivPhoto = (ImageView) v.findViewById(R.id.ivPhoto);
		
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		ougListener = (OnUpdateGuiListener) getActivity();
		
		if(getArguments() != null)
			photo = getArguments().getParcelable("data");
	}

	@Override
	public void onStart() {
		super.onStart();
		
		if(photo.getImage() == null) {
			
			HttpHelper hHelper = new HttpHelper(new Handler(), new OnServerAnswerListener() {
				
				@Override
				public void onServerAnswer(int type, int status, Bundle answer) {
					
					if(status == AppConstants.STATUS_SUCCESS) {
						
						photo.setImage((Bitmap) answer.getParcelable(AppConstants.IMAGE));
						ivPhoto.setImageBitmap(photo.getImage());
						ougListener.onUpdateGui(AppConstants.ACTION_FULL_PHOTO_UPDATE, null);
					}
//						Log.d("myLogs", "GalleryItemFragment.onStart: --- " + answer);
				}
			});
			
			hHelper.loadPhoto(photo);
		}
		ivPhoto.setImageBitmap(photo.getImage());
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		
		// TODO remember loaded full photo
		
	}
	
	
	
	
	
	
}
