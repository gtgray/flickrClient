package com.core.flickrclient;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class PhotoPage implements Parcelable {
	
	private int page;
	private int perPage;
	private String pages;
	private ArrayList<Photo> photos;
	
	public PhotoPage(int page, int perPage, String pages) {
		
		this.page = page;
		this.perPage = perPage;
		this.pages = pages;
	}

	public int getPage() {
		return page;
	}

	public int getPerPage() {
		return perPage;
	}

	public String getPages() {
		return pages;
	}
	
	public ArrayList<Photo> getPhotos() {
		return photos;
	}
	
	public Photo getPhotoById(String id) {
		
		if(id == null) return null;
		
		for(Photo p : photos) {
			
			if(p.getId().equals(id)) return p;
		}
		
		return null;
	}
	
	public void setPhotos(ArrayList<Photo> photos) {
		this.photos = photos;
	}
	

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeInt(page);
		dest.writeInt(perPage);
		dest.writeString(pages);
		dest.writeTypedList(photos);
	}
	
	public static final Parcelable.Creator<PhotoPage> CREATOR = new Parcelable.Creator<PhotoPage>() {
		
	    public PhotoPage createFromParcel(Parcel in) {
	      return new PhotoPage(in);
	    }

	    public PhotoPage[] newArray(int size) {
	      return new PhotoPage[size];
	    }
	    
	  };
	  
	  private PhotoPage() {
		  photos = new ArrayList<Photo>();
	  }

	  private PhotoPage(Parcel parcel) {
		this();
		
	    this.page = parcel.readInt();
	    this.perPage = parcel.readInt();
	    this.pages = parcel.readString();
	    parcel.readTypedList(this.photos, Photo.CREATOR);
	  }
	  
	  
}
