package app;

import android.app.Application;
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
	
	/*
	private static ProgressDialog progress;
	
	public static void setNewProgressDialog(Context context){
		progress = new ProgressDialog(context);
		progress.setIndeterminate(true);
	}
	
	public static void setProgressIndicator(boolean visible){
		setProgressIndicator(visible, singleton.getResources().getString(R.string.loading_default));
	}

	public static void setProgressIndicator(boolean visible, String message){
		if(visible){
			progress.setMessage(message);
			if(!progress.isShowing()){
				progress.show();
			}
		} else {
			if(progress.isShowing()){
				progress.dismiss();
			}
			progress.setMessage("");	
		}
	}*/
	
	@Override
	public void onCreate(){
		super.onCreate();
		singleton = this;
		client = new ClaroClient();
		Repository.SetOpenHelper(getApplicationContext());
	}

}
