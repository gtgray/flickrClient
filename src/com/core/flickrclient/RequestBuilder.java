package com.core.flickrclient;

import android.util.Log;


public class RequestBuilder {
	
	public final static String SERVER_LIST_URL = "https://api.flickr.com/services/rest/?";
	public final static String SERVER_API_KEY = "api_key=";
	public final static String SERVER_LIST_METHOD = "&method=flickr.interestingness.getList";
	public final static String SERVER_LIST_FORMAT = "&format=json";
	public final static String SERVER_LIST_PERPAGE = "&per_page=";
	public final static String SERVER_LIST_PAGE = "&page=";
	
	public final static String SERVER_PHOTO_URL1 = "https://farm";
	public final static String SERVER_PHOTO_URL2 = ".staticflickr.com/";
	public final static String SERVER_PHOTO_URL3 = "_s";
	public final static String SERVER_PHOTO_EXT = ".jpg";
	
	
	public static String getListRequest(String key, int perPage, int page) {
		
		StringBuilder sb = new StringBuilder(SERVER_LIST_URL);
		
		sb.append(SERVER_API_KEY);
		sb.append(key);
		sb.append(SERVER_LIST_METHOD);
		sb.append(SERVER_LIST_FORMAT);
		
		if(perPage > 0) {
			
			sb.append(SERVER_LIST_PERPAGE);
			sb.append(perPage);
		}
		
		if(page > 0) {
			
			sb.append(SERVER_LIST_PAGE);
			sb.append(page);
		}
		
		Log.d("myLogs", "RequestBuilder.getListRequest: sb " + sb);
		
		return sb.toString();
	}
	
	public static String getPreviewRequest(Photo preview) {
		
		StringBuilder sb = new StringBuilder(SERVER_PHOTO_URL1);
		sb.append(preview.getFarm());
		sb.append(SERVER_PHOTO_URL2);
		sb.append(preview.getServer());
		sb.append("/");
		sb.append(preview.getId());
		sb.append("_");
		sb.append(preview.getSecret());
		sb.append(SERVER_PHOTO_URL3);
		sb.append(SERVER_PHOTO_EXT);
		
		Log.d("myLogs", "RequestBuilder.getPreviewRequest: sb " + sb);
		
		return sb.toString();
	}
	
	public static String getPhotoRequest(Photo photo) {
		
		StringBuilder sb = new StringBuilder(SERVER_PHOTO_URL1);
		sb.append(photo.getFarm());
		sb.append(SERVER_PHOTO_URL2);
		sb.append(photo.getServer());
		sb.append("/");
		sb.append(photo.getId());
		sb.append("_");
		sb.append(photo.getSecret());
		sb.append(SERVER_PHOTO_EXT);
		
		Log.d("myLogs", "RequestBuilder.getPhotoRequest: sb " + sb);
		
		return sb.toString();
	}
	
	
	
	
}
