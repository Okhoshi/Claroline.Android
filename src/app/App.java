/**
 * Claroline Mobile - Android
 * 
 * @package     app
 * 
 * @author      Devos Quentin (q.devos@student.uclouvain.be)
 * @version     1.0
 *
 * @license     ##LICENSE##
 * @copyright   2013 - Devos Quentin
 */
package app;

import java.io.File;

import model.Cours;
import net.claroline.mobile.android.R;

import org.joda.time.DateTime;

import util.UtilDateTimeTypeConverter;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.activeandroid.app.Application;
import com.activeandroid.query.Delete;
import com.activeandroid.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import connectivity.ClarolineClient;

/**
 * Claroline Mobile - Android
 * 
 * Global {@link android.app.Application}.
 * 
 * @author Devos Quentin
 * @version 1.0
 */
public class App extends Application {

	/**
	 * Shared Preference tag.
	 */
	public static final String SETTINGS_USER_PASSWORD = "user_password";
	/**
	 * Shared Preference tag.
	 */
	public static final String SETTINGS_IS_PLATFORM_ADMIN = "isPlatformAdmin";
	/**
	 * Shared Preference tag.
	 */
	public static final String SETTINGS_FIRST_NAME = "firstName";
	/**
	 * Shared Preference tag.
	 */
	public static final String SETTINGS_PICTURE = "picture";
	/**
	 * Shared Preference tag.
	 */
	public static final String SETTINGS_USER_IMAGE = "userImage";
	/**
	 * Shared Preference tag.
	 */
	public static final String SETTINGS_INSTITUTION_NAME = "institutionName";
	/**
	 * Shared Preference tag.
	 */
	public static final String SETTINGS_PLATFORM_NAME = "platformName";
	/**
	 * Shared Preference tag.
	 */
	public static final String SETTINGS_OFFICIAL_CODE = "officialCode";
	/**
	 * Shared Preference tag.
	 */
	public static final String SETTINGS_LAST_NAME = "lastName";
	/**
	 * Shared Preference tag.
	 */
	public static final String SETTINGS_PLATFORM_HOST = "platform_host";
	/**
	 * Shared Preference tag.
	 */
	public static final String SETTINGS_PLATFORM_MODULE = "platform_module";
	/**
	 * Shared Preference tag.
	 */
	public static final String SETTINGS_USER_LOGIN = "user_login";

	/**
	 * Shared Preference tag.
	 */
	public static final String SETTINGS_ACCOUNT_VERIFIED = "account verified";

	/**
	 * Singleton instance.
	 */
	private static App sSingleton;

	/**
	 * @return the Download folder specific to this app.
	 */
	public static File getDownloadFolder() {
		File root = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

		File dir = new File(root.getAbsolutePath() + "/"
				+ sSingleton.getResources().getString(R.string.app_name));
		return dir;
	}

	/**
	 * @return the custom GSON instance
	 */
	public static Gson getGSON() {
		return getInstance().mGson;
	}

	/**
	 * @return the App instance
	 */
	public static App getInstance() {
		return sSingleton;
	}

	/**
	 * @return the {@link SharedPreferences} manager
	 */
	public static SharedPreferences getPrefs() {
		return PreferenceManager.getDefaultSharedPreferences(sSingleton
				.getApplicationContext());
	}

	/**
	 * Removes all settings related to the current account.
	 * 
	 * @param calledFromClient
	 *            if it is called from client
	 */
	public static void invalidateUser(final boolean calledFromClient) {
		if (!calledFromClient) {
			ClarolineClient.getInstance().invalidateClient(true);
		}
		new Delete().from(Cours.class).execute();
		getPrefs().edit().remove(App.SETTINGS_USER_LOGIN)
				.remove(App.SETTINGS_USER_PASSWORD)
				.remove(App.SETTINGS_FIRST_NAME).remove(App.SETTINGS_LAST_NAME)
				.remove(App.SETTINGS_IS_PLATFORM_ADMIN)
				.remove(App.SETTINGS_OFFICIAL_CODE)
				.remove(App.SETTINGS_PLATFORM_NAME)
				.remove(App.SETTINGS_INSTITUTION_NAME)
				.remove(App.SETTINGS_USER_IMAGE).remove(App.SETTINGS_PICTURE)
				.remove(App.SETTINGS_ACCOUNT_VERIFIED).apply();
	}

	/**
	 * @param apiLevel
	 *            the API level to test against
	 * @return <code>true</code> if the current API level is greater of equal to
	 *         <code>apiLevel</code>, <code>false</code> otherwise
	 */
	public static boolean isNewerAPI(final int apiLevel) {
		return Build.VERSION.SDK_INT >= apiLevel;
	}

	/**
	 * 
	 * @return the Internet access state
	 */
	public static boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getInstance()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	/**
	 * Custom Gson JSON deserializer.
	 */
	private Gson mGson;
	public static final int MIN_VERSION = 1;

	@Override
	public void onCreate() {
		super.onCreate();
		sSingleton = this;
		mGson = new GsonBuilder().registerTypeAdapter(DateTime.class,
				new UtilDateTimeTypeConverter()).create();
		Log.setEnabled(false);
	}
}
