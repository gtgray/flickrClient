package com.core.flickrclient;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class ResponseParser {
	
	public final static String PHOTOS = "photos";
	public final static String PAGE = "page";	
	public final static String PAGES = "pages";
	public final static String PERPAGE = "perpage";
	public final static String PHOTO = "photo";
	
	public final static String STAT = "stat";
	public final static String CODE = "code";
	public final static String MESSAGE = "message";
	
	public final static String PHOTO_ID = "id";
	public final static String PHOTO_FARM = "farm";
	public final static String PHOTO_SERVER = "server";
	public final static String PHOTO_SECRET = "secret";
	public final static String PHOTO_TITLE = "title";
	
/*	
	{ "stat": "fail", 
		"code": 1, 
		"message": "Not a valid date string"
	}
	
	{ "photos":  { "page": 1, 
					"pages": "250", 
					"perpage": 2, 
					"total": "500", 
					"photo": [
					         { "id": "14248636708", 
					        	"owner": "42807061@N08", 
					        	"secret": "b2b2359aa3", 
					        	"server": "3850", 
					        	"farm": 4, 
					        	"title": "sunny blues with 2 skinny legs", 
					        	"ispublic": 1, 
					        	"isfriend": 0, 
					        	"isfamily": 0 },
					         { "id": "14456112873", 
					         	"owner": "9863477@N06", 
					         	"secret": "d66bf22fed", 
					         	"server": "5495", 
					         	"farm": 6, 
					         	"title": "City Slickers", 
					         	"ispublic": 1, 
					         	"isfriend": 0, 
					         	"isfamily": 0 }
	    			] }, 
	    "stat": "ok" }
*/	
	
	public static Bundle parseListResponse(String json) {
		
		if(json == null) return null;
		
		String j = json.substring("jsonFlickrApi(".length(), (json.length() - 1));
		
		Bundle data = new Bundle();
		
		try {
			JSONObject wrapped = new JSONObject(j);
			
			data = removeWrapper(wrapped);
			
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
		
		return data;
	}
	
	private static Bundle removeWrapper(JSONObject wrapped) {
		
		Bundle unwrapped = new Bundle();
		
		try {
			JSONObject jPage = wrapped.getJSONObject(PHOTOS);
			
			int page = jPage.getInt(PAGE);
			int perPage = jPage.getInt(PERPAGE);
			String pages = jPage.getString(PAGES);
			PhotoPage photoPage = new PhotoPage(page, perPage, pages);
			photoPage.setPhotos(getListPhotos(jPage.getString(PHOTO)));
			
			unwrapped.putParcelable(AppConstants.PHOTO_PAGE, photoPage);
			
		} catch (JSONException e) {
			
			try {
				
				String stat = wrapped.getString(STAT);
				int code = wrapped.getInt(CODE);
				String message = wrapped.getString(MESSAGE);
				
				unwrapped.putParcelable(AppConstants.MISTAKE, new Mistake(stat, code, message));
				
			} catch (JSONException ee) {
				
				ee.printStackTrace();
				return null;
			}
		}
		
		return unwrapped;
	}
	
	private static ArrayList<Photo> getListPhotos(String photos) {
		
		if(photos == null) return null;
		
		ArrayList<Photo> list = new ArrayList<Photo>();
		
		try {
			JSONArray array = new JSONArray(photos);
			
			for(int i = 0; i < array.length(); i++) {
				
				String id = array.getJSONObject(i).getString(PHOTO_ID);
				int farm = array.getJSONObject(i).getInt(PHOTO_FARM);
				String server = array.getJSONObject(i).getString(PHOTO_SERVER);
				String secret = array.getJSONObject(i).getString(PHOTO_SECRET);
				String title = array.getJSONObject(i).getString(PHOTO_TITLE);
				
				list.add(new Photo(id, farm, server, secret, title));
			}
			
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
		
		return list;
	}
	
	public static byte[] inputStreamToByteArray(InputStream is) {
		
		if(is == null) return null;
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		byte[] buffer = new byte[1024 * 128]; // 128k bytes
		
		try {
			BufferedInputStream bis = new BufferedInputStream(is);
			
			int bytes;
			
			while((bytes = bis.read(buffer)) != -1) {
				
				stream.write(buffer, 0, bytes);
			}
			
			bis.close();
			bis = null;
			
		} catch (Exception e) {
			
			e.getStackTrace();
			return null;
		}
		
		byte[] bytes = stream.toByteArray();
		
		return bytes;
	}
	
	public static Bitmap inputStreamToBitmap(InputStream is) {
	
		try {
			BufferedInputStream bis = new BufferedInputStream(is);
			
			Bitmap bitmap = BitmapFactory.decodeStream(bis);
			
			bis.close();
			bis = null;
			
			return bitmap;
			
		} catch(IOException e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	
}
