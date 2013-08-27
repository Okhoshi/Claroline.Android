package app;

import java.io.File;

import net.claroline.mobile.android.R;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.activeandroid.app.Application;

public class App extends Application {

	/**
	 * Shared Preference tag.
	 */
	public static final String SETTINGS_COOKIES_EXPIRES = "cookies_expires";
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
	 * Singleton instance.
	 */
	private static App sSingleton;

	/*
	 * public static ClaroClient getClient(final Handler handler, final
	 * SupportedModules mod, final SupportedMethods op) { return
	 * getClient(handler, mod, op, "", ""); }
	 * 
	 * public static ClaroClient getClient(final Handler handler, final
	 * SupportedModules mod, final SupportedMethods op, final String syscode) {
	 * return getClient(handler, mod, op, syscode, ""); }
	 * 
	 * public static ClaroClient getClient(final Handler handler, final
	 * SupportedModules mod, final SupportedMethods op, final String syscode,
	 * final String resource) { return new ClaroClient(handler, mod, op,
	 * syscode, resource); }
	 */

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

	public static App getInstance() {
		return sSingleton;
	}

	public static SharedPreferences getPrefs() {
		return PreferenceManager.getDefaultSharedPreferences(sSingleton
				.getApplicationContext());
	}

	public static boolean isNewerAPI(final int apiLevel) {
		return Build.VERSION.SDK_INT >= apiLevel;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sSingleton = this;
	}
}
