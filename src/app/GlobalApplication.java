package app;

import connectivity.ClaroClient;
import android.app.Application;

public class GlobalApplication extends Application {

	private static GlobalApplication singleton;
	
	public static GlobalApplication getInstance(){
		return singleton;
	}
	
	private static ClaroClient client;
	
	public static ClaroClient getClient(){
		return client;
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		singleton = this;
		client = new ClaroClient();
	}

}
