package com.freer.infusion;

import android.app.Application;

public class App extends Application{
	
	private static App mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		
		this.mContext = this;
	}
	
	public static App getAppContext() {
		return mContext;
	}
}
