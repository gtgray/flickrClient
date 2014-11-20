package com.core.flickrclient;

import android.os.Parcel;
import android.os.Parcelable;

public class Mistake implements Parcelable {
	
	private String stat;
	private int code;
	private String message;
	
	public Mistake(String stat, int code, String message) {
		
		this.stat = stat;
		this.code = code;
		this.message = message;
	}

	public String getStat() {
		return stat;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder("Stat: ");
		sb.append(stat);
		sb.append(", Error code: ");
		sb.append(code);
		sb.append(", Message: ");
		
		return sb.toString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(stat);
		dest.writeInt(code);
		dest.writeString(message);
	}
	
	public static final Parcelable.Creator<Mistake> CREATOR = new Parcelable.Creator<Mistake>() {
		
	    public Mistake createFromParcel(Parcel in) {
	      return new Mistake(in);
	    }

	    public Mistake[] newArray(int size) {
	      return new Mistake[size];
	    }
	    
	  };

	  private Mistake(Parcel parcel) {
		  
	    this.stat = parcel.readString();
	    this.code = parcel.readInt();
	    this.message = parcel.readString();
	  }
	
}
