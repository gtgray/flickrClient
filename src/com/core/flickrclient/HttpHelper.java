package com.core.flickrclient;

import java.io.InputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;

public class HttpHelper {
	
	public final static int TYPE_LIST = 0x00000001;
	public final static int TYPE_PREVIEW = 0x00000002;
	public final static int TYPE_PHOTO = 0x00000004;
	
	private OnServerAnswerListener osaListener;	
	
	private Handler handler;
	
	
	public HttpHelper(Handler handler, OnServerAnswerListener listener) {
		
		this.handler = handler;
		this.osaListener = listener;
	}
	
	public Handler getHandler() {
		
		return this.handler;
	}
	
	public OnServerAnswerListener getOnServerAnswerListener() {
		
		return this.osaListener;
	}
	
	public void loadPhotoList(String key, int perPage, int page) {
		
		HttpUriRequest request = new HttpGet(RequestBuilder.getListRequest(key, perPage, page));
		executeTalking(TYPE_LIST, request);
	}
	
	public void loadPreview(Photo preview) {
		
		HttpUriRequest request = new HttpGet(RequestBuilder.getPreviewRequest(preview));
		executeTalking(TYPE_PREVIEW, request);
	}
	
	public void loadPhoto(Photo photo) {
		
		HttpUriRequest request = new HttpGet(RequestBuilder.getPhotoRequest(photo));
		executeTalking(TYPE_PHOTO, request);
	}
	
	private void executeTalking(final int type, final HttpUriRequest request) {
		
		if(request == null) return;
		
		Thread worker = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
//				AndroidHttpClient httpClient = AndroidHttpClient.newInstance(MainConstants.USER_AGENT);
				DefaultHttpClient client = new DefaultHttpClient();
				
				// very very tricky code from stackoverflow
				// helps to avoid sslCertificate untrusted exception
				HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
				SchemeRegistry registry = new SchemeRegistry();
				SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
				socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
				registry.register(new Scheme("https", socketFactory, 443));
				SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
				DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
				HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);				
				
				HttpResponse response = null;
				
				try {
					
					response = httpClient.execute(request);
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				
				final int status = checkStatusCode(response);
				final Bundle answer = parseAnswer(type, response);
				
				if(getHandler() != null) {
					
					getHandler().post(new Runnable() {
						
						@Override
						public void run() {
							
							if(getOnServerAnswerListener() != null)
								getOnServerAnswerListener().onServerAnswer(type, status, answer);
						}
					});
				}
//				httpClient.close();
			}
		});
		worker.start();
	}
	
	private Bundle parseAnswer(int type, HttpResponse response) {
		
		if(response == null) return null;
		
		HttpEntity entity = response.getEntity();
		
		if(entity == null) return null;
		
		try {
			return dataToBundle(type, entity.getContent());
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	private Bundle dataToBundle(int type, InputStream data) {
		
		if(data == null) return null;
		
		Bundle parsed = new Bundle();
		
		switch (type) {
		
		case TYPE_LIST:
			
			String json = new String(ResponseParser.inputStreamToByteArray(data));
			parsed = ResponseParser.parseListResponse(json);
			
			break;
			
		case TYPE_PREVIEW:
		case TYPE_PHOTO:
			
			Bitmap bitmap = ResponseParser.inputStreamToBitmap(data);
			
			if(bitmap == null) return null;
			
			parsed.putParcelable(AppConstants.IMAGE, bitmap);
			
			break;
		}
		
		return parsed;
	}

	protected int checkStatusCode(HttpResponse response) {
		
		if(response == null) return AppConstants.STATUS_FAILURE;
		
		int statusCode = response.getStatusLine().getStatusCode();
		
		if((statusCode > 199) && (statusCode < 210)) return AppConstants.STATUS_SUCCESS;
		
		return AppConstants.STATUS_FAILURE;
	}
	

	
	
}
