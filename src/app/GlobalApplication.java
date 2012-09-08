package app;

import mobile.claroline.R;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import connectivity.ClaroClient;
import dataStorage.Repository;

public class GlobalApplication extends Application {

	private static GlobalApplication singleton;
	
	public static GlobalApplication getInstance(){
		return singleton;
	}
	
	private static ClaroClient client;
	
	public static ClaroClient getClient(){
		return client;
	}
	
	public static SharedPreferences getPreferences(){
		return PreferenceManager.getDefaultSharedPreferences(singleton.getApplicationContext());
	}
	
	private static ProgressDialog progress;
	
	public static void setProgressIndicator(boolean visible){
		setProgressIndicator(null, visible);
	}
	
	public static void setProgressIndicator(boolean visible, String message){
		setProgressIndicator(null, visible, message);
	}

	public static void setProgressIndicator(Context context, boolean visible) {
		setProgressIndicator(context, visible, singleton.getResources().getString(R.string.loading_default));
	}

	public static void setProgressIndicator(Context context, boolean visible, String message){
		if(visible){
			if(progress == null){
				progress = new ProgressDialog(context);
				progress.setIndeterminate(true);
			}
			progress.setMessage(message);
			if(!progress.isShowing()){
				progress.show();
			}
		} else {
			progress.dismiss();
			progress = null;
		}
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		singleton = this;
		client = new ClaroClient();
		Repository.SetOpenHelper(getApplicationContext());
	}
}
