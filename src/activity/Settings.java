/**
 * Claroline Mobile - Android
 * 
 * @package     activity
 * 
 * @author      Devos Quentin (q.devos@student.uclouvain.be)
 * @version     1.0
 *
 * @license     ##LICENSE##
 * @copyright   2013 - Devos Quentin
 */
package activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import net.claroline.mobile.android.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import app.App;
import app.AppPreferenceActivity;

/**
 * Claroline Mobile - Android
 * 
 * Settings activity.
 * 
 * @author Devos Quentin (q.devos@student.uclouvain.be)
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class Settings extends AppPreferenceActivity implements
		OnSharedPreferenceChangeListener {

	/**
	 * On Screen settings.
	 */
	private static final ArrayList<String> ON_SCREEN_SETTINGS = new ArrayList<String>(
			Arrays.asList(new String[] { App.SETTINGS_PLATFORM_HOST,
					App.SETTINGS_PLATFORM_MODULE }));

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		Map<String, ?> keys = App.getPrefs().getAll();

		for (Map.Entry<String, ?> entry : keys.entrySet()) {
			if (ON_SCREEN_SETTINGS.contains(entry.getKey())) {
				Log.d("map values", entry.getKey() + ": "
						+ entry.getValue().toString());
				Preference pref = findPreference(entry.getKey());
				// Set summary to be the user-description for the selected value
				pref.setSummary(App.getPrefs().getString(entry.getKey(), ""));
			}
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(
			final SharedPreferences sharedPreferences, final String key) {
		if (!key.contains("password")) {
			Preference pref = findPreference(key);
			if (pref != null) {
				// Set summary to be the user-description for the selected value
				pref.setSummary(sharedPreferences.getString(key, ""));
			}
		}

		if (ON_SCREEN_SETTINGS.contains(key)) {
			App.invalidateUser(false);
		}
	}

	@Override
	public void refreshUI() {
		// Nothing to do
	}
}
