package app;

import model.OldCours;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.activeandroid.app.Application;

import connectivity.AllowedOperations;
import connectivity.ClaroClient;

public class App extends Application {

	private static App singleton;

	public static ClaroClient getClient(final Handler handler,
			final AllowedOperations op) {
		return getClient(handler, op, null, -1);
	}

	public static ClaroClient getClient(final Handler handler,
			final AllowedOperations op, final OldCours reqCours) {
		return getClient(handler, op, reqCours, -1);
	}

	public static ClaroClient getClient(final Handler handler,
			final AllowedOperations op, final OldCours reqCours, final int resId) {
		return new ClaroClient(handler, op, reqCours, resId);
	}

	public static App getInstance() {
		return singleton;
	}

	public static SharedPreferences getPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(singleton
				.getApplicationContext());
	}

	public static boolean isNewerAPI(final int apiLevel) {
		return Build.VERSION.SDK_INT >= apiLevel;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
	}
}
