package app;

import mobile.claroline.R;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import connectivity.AllowedOperations;
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
		setProgressIndicator(null, visible, message, true, 0);
	}

	public static void setProgressIndicator(Context context, boolean visible) {
		setProgressIndicator(context, visible, singleton.getResources().getString(R.string.loading_default), true, 0);
	}
	
	public static void setProgressIndicator(Context context, boolean visible, boolean isIndeterminate, int max) {
		setProgressIndicator(context, visible, singleton.getResources().getString(R.string.loading_default), isIndeterminate, max);
	}
	
	public static void setProgressIndicator(Context context, boolean visible, String message, boolean isIndeterminate, int max, String format){
		setProgressIndicator(context, visible, message, isIndeterminate, max);
		progress.setProgressNumberFormat(format);
	}

	public static void setProgressIndicator(Context context, boolean visible, String message, boolean isIndeterminate, int max){
		if(visible){
			if(progress == null){
				progress = new ProgressDialog(context);
				progress.setCancelable(false);
				progress.setIndeterminate(isIndeterminate);
			} else if(progress.isIndeterminate() != isIndeterminate){
				progress.dismiss();
				progress = new ProgressDialog(progress.getContext());
				progress.setCancelable(false);
				progress.setIndeterminate(isIndeterminate);
			}
			if(!isIndeterminate){
				progress.setMax(max);
				progress.setProgress(0);
				progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
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
	
	public static void incrementProgression(int value){
		if(progress != null && progress.isShowing() && !progress.isIndeterminate()){
			progress.incrementProgressBy(value - progress.getProgress());
		}
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		singleton = this;
		client = new ClaroClient();
		(new Thread(client.makeOperation(null, AllowedOperations.getUserData))).start();
		Repository.SetOpenHelper(getApplicationContext());
	}
}
