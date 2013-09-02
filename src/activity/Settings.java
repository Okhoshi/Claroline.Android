/**
 * 
 */
package activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import net.claroline.mobile.android.R;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import app.App;

/**
 * @author Quentin
 * 
 */
public class Settings extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	private ArrayList<String> mOnScreenSettings = new ArrayList<String>(
			Arrays.asList(new String[] { App.SETTINGS_PLATFORM_HOST,
					App.SETTINGS_PLATFORM_MODULE }));

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		if (App.isNewerAPI(Build.VERSION_CODES.HONEYCOMB)) {
			setActionBar();
		}

		Map<String, ?> keys = App.getPrefs().getAll();

		for (Map.Entry<String, ?> entry : keys.entrySet()) {
			if (mOnScreenSettings.contains(entry.getKey())) {
				Log.d("map values", entry.getKey() + ": "
						+ entry.getValue().toString());
				Preference pref = findPreference(entry.getKey());
				// Set summary to be the user-description for the selected value
				pref.setSummary(App.getPrefs().getString(entry.getKey(), ""));
			}
		}

	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(
			final SharedPreferences sharedPreferences, final String key) {
		if (!key.contains("password")) {
			Preference pref = findPreference(key);
			// Set summary to be the user-description for the selected value
			pref.setSummary(sharedPreferences.getString(key, ""));
		}
	}

	/**
	 * Sets up the ActionBar.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

}
