package com.core.flickrclient;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {
	
	private String id;
	private int farm;
	private String server;
	private String secret;
	private String title;
	private Bitmap preview;
	private Bitmap image;
	private boolean checked = false;
	
	public Photo(String id, int farm, String server, String secret, String title) {
		
		this.id = id;
		this.farm = farm;
		this.server = server;
		this.secret = secret;
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public int getFarm() {
		return farm;
	}

	public String getServer() {
		return server;
	}

	public String getSecret() {
		return secret;
	}

	public String getTitle() {
		return title;
	}
	
	public Bitmap getPreview() {
		return preview;
	}
	
	public void setPreview(Bitmap preview) {
		this.preview = preview;
	}
	
	public Bitmap getImage() {
		return image;
	}
	
	public void setImage(Bitmap image) {
		this.image = image;
	}
	
	public boolean isChecked() {
		return checked;
	}
	
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(id);
		dest.writeInt(farm);
		dest.writeString(server);
		dest.writeString(secret);
		dest.writeString(title);
		dest.writeParcelable(preview, 0);
		dest.writeParcelable(image, 0);
		dest.writeByte((byte) (checked ? 1 : 0));
	}
	
	public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
		
	    public Photo createFromParcel(Parcel in) {
	      return new Photo(in);
	    }

	    public Photo[] newArray(int size) {
	      return new Photo[size];
	    }
	    
	  };

	  private Photo(Parcel parcel) {
		  
	    this.id = parcel.readString();
	    this.farm = parcel.readInt();
	    this.server = parcel.readString();
	    this.secret = parcel.readString();
	    this.title = parcel.readString();
	    this.preview = parcel.readParcelable(null);
	    this.image = parcel.readParcelable(null);
	    this.checked = (parcel.readByte() != 0);
	  }
	
	
	
}
