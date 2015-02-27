package com.airmh.soundllysdktest;

import android.app.Application;
import android.content.Context;

import com.soundlly.sdk.SoundllyCore;

/**
 * 	Application Class
 * 	Soundlly init과 전역으로 Context를 사용하기 위한 용도로 사용한다.
 * @author AirMH
 *
 */
public class MainApplication extends Application{
	
	private static Context appContext = null;
	public static final String API_KEY = "54c5c5b4-0a9205df-da905901-d05fifb8";
	public static final String BIND_SUCCESS_ACTION = "BIND_SUCCESS_ACTION";
	public static final String SEND_RECEIVE_INTENT = "SEND_RECEIVE_INTENT";

	@Override
	public void onCreate() {
		super.onCreate();
		appContext = this;
		// SDK 초기화.
		SoundllyCore.init(getApplicationContext());
	}
	
	public static Context getContext() {
		return appContext;
	}

}